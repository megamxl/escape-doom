package main

import (
	"bufio"
	"bytes"
	"errors"
	"fmt"
	"io"
	"log"
	"os"
	"os/exec"
	"strings"
	"time"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

type output struct {
	out []byte
	err error
}

func ReadConfig(configFile string) kafka.ConfigMap {

	m := make(map[string]kafka.ConfigValue)

	file, err := os.Open(configFile)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to open file: %s", err)
		os.Exit(1)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := strings.TrimSpace(scanner.Text())
		if !strings.HasPrefix(line, "#") && len(line) != 0 {
			kv := strings.Split(line, "=")
			parameter := strings.TrimSpace(kv[0])
			value := strings.TrimSpace(kv[1])
			m[parameter] = value
		}
	}

	if err := scanner.Err(); err != nil {
		fmt.Printf("Failed to read file: %s", err)
		os.Exit(1)
	}

	return m

}

func setupForExecution(input *Request, configMap kafka.ConfigMap) string {

	shouldExecute := true

	//make directory
	if _, err := os.Stat(input.PlayerSessionId); errors.Is(err, os.ErrNotExist) {
		err := os.Mkdir(input.PlayerSessionId, os.ModePerm)
		if err != nil {
			shouldExecute = false
			fmt.Println("failed Making the directory")
			log.Println(err)
		}
	}

	//make Language Docker File
	//TODO make it customizable for all case
	dockerfile := "java.Dockerfile"
	samplefile := "app.java"
	switch {
	case input.Language == "Java":
		dockerfile = "java.Dockerfile"
		samplefile = "app.java"
	case input.Language == "Javascript":
		dockerfile = "javascript.Dockerfile"
		samplefile = "app.js"
	case input.Language == "Python":
		dockerfile = "python.Dockerfile"
		samplefile = "app.py"
	}

	copy(dockerfile, input.PlayerSessionId+"/"+".Dockerfile")

	filename := input.PlayerSessionId + "/" + samplefile

	var _, err = os.Stat(filename)
	//make code File
	if os.IsNotExist(err) {
		file, err := os.Create(filename)
		if err != nil {
			fmt.Println(err)
			shouldExecute = false
		}
		os.WriteFile(file.Name(), []byte(input.Code), 0644)
		defer file.Close()
	} else {
		fmt.Println("File already exists!", filename)
		shouldExecute = false
	}

	curr := ""

	//call Docker
	if shouldExecute {
		curr = executeDocker(".Dockerfile", input.PlayerSessionId)
	}

	err23 := os.RemoveAll(input.PlayerSessionId + "/")
	if err23 != nil {
		fmt.Println("lel not deleted")
	}

	sendMessage("computedCode", configMap, input, curr)

	return curr
}

// TODO MAKE ALL RETURNS USEFUL
func executeDocker(dockerFileName string, name string) string {

	fmt.Println("Welcome")
	dockerBuild := exec.Command("docker", "build", "-t", name, "-f", dockerFileName, ".")
	dockerBuild.Dir = name + "/"
	var errb bytes.Buffer
	dockerBuild.Stderr = &errb
	dockerBuild.Stderr = os.Stderr
	//dockerBuild.Stdout = &outb

	_, err := dockerBuild.Output()
	if err != nil {
		// if there was any error, print it here
		fmt.Println("could not run command: ")

		return "COMPILE ERROR"
	}
	// otherwise, print the output from running the command

	ch := make(chan output)

	go func() {
		cmd := exec.Command("docker", "run", "--rm", name)
		out, err := cmd.CombinedOutput()
		ch <- output{out, err}
	}()

	select {
	case <-time.After(2 * time.Minute):
		fmt.Println("Should never be here Log and kill all dockres")
	case x := <-ch:
		fmt.Println("here")

		if len(string(x.out)) > 1000 {
			x.out = []byte("wouldOverflow")
		}

		fmt.Printf("program done; out: %q\n", string(x.out))
		if x.err != nil {
			fmt.Printf("program errored: %s\n", x.err)
		}

		dockerDeleteImage := exec.Command("docker", "rmi", name)
		dockerDeleteImage.Stderr = os.Stderr

		_, err234 := dockerDeleteImage.Output()
		if err234 != nil {
			// if there was any error, print it here
			fmt.Println("could not run command: ", err)
		}
		return string(x.out)
	}
	return ""
}

func copy(src, dst string) (int64, error) {
	sourceFileStat, err := os.Stat(src)
	if err != nil {
		return 0, err
	}

	if !sourceFileStat.Mode().IsRegular() {
		return 0, fmt.Errorf("%s is not a regular file", src)
	}

	source, err := os.Open(src)
	if err != nil {
		return 0, err
	}
	defer source.Close()

	destination, err := os.Create(dst)
	if err != nil {
		return 0, err
	}
	defer destination.Close()
	nBytes, err := io.Copy(destination, source)
	return nBytes, err
}
