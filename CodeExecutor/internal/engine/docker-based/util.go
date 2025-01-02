package docker_based

import (
	"bytes"
	"errors"
	"fmt"
	"io"
	"log"
	"os"
	"os/exec"
	"time"

	"github.com/megamxl/escape-doom/CodeExecutor/internal/constants"
)

type DockerEngine struct {
}

type output struct {
	out []byte
	err error
}

func (d DockerEngine) ExecuteCode(input *constants.Request) string {

	shouldExecute := true

	shouldExecute = makeDirectory(input, shouldExecute)

	dockerfile, samplefile := GetDockerLanguageParameters(input)

	copy(dockerfile, input.PlayerSessionId+"/"+".Dockerfile")

	shouldExecute = createFileForExecution(input, samplefile, shouldExecute)

	curr := ""

	//call Docker
	if shouldExecute {
		curr = executeDocker(".Dockerfile", input.PlayerSessionId)
	}

	err23 := os.RemoveAll(input.PlayerSessionId + "/")
	if err23 != nil {
		log.Printf("Cant delte folder for %s", input.PlayerSessionId)
	}

	return curr
}

func makeDirectory(input *constants.Request, shouldExecute bool) bool {
	//make directory
	if _, err := os.Stat(input.PlayerSessionId); errors.Is(err, os.ErrNotExist) {
		err := os.Mkdir(input.PlayerSessionId, os.ModePerm)
		if err != nil {
			shouldExecute = false
			log.Println("failed Making the directory")
			log.Println(err)
		}
	}
	return shouldExecute
}

func createFileForExecution(input *constants.Request, samplefile string, shouldExecute bool) bool {
	filename := input.PlayerSessionId + "/" + samplefile

	var _, err = os.Stat(filename)
	//make code File
	if os.IsNotExist(err) {
		file, err := os.Create(filename)
		if err != nil {
			log.Println(err)
			shouldExecute = false
		}
		os.WriteFile(file.Name(), []byte(input.Code), 0644)
		defer file.Close()
	} else {
		log.Println("File already exists!", filename)
		shouldExecute = false
	}
	return shouldExecute
}

func GetDockerLanguageParameters(input *constants.Request) (string, string) {
	//make Language Docker File
	//TODO make it customizable for all case
	dockerfile := "java.Dockerfile"
	samplefile := "app.java"
	switch {
	case input.Language == "JAVA":
		dockerfile = "internal/engine/docker-based/java.Dockerfile"
		samplefile = "app.java"
	case input.Language == "JAVASCRIPT":
		dockerfile = "internal/engine/docker-based/javascript.Dockerfile"
		samplefile = "app.js"
	case input.Language == "PYTHON":
		dockerfile = "internal/engine/docker-based/python.Dockerfile"
		samplefile = "app.py"
	}
	return dockerfile, samplefile
}

func executeDocker(dockerFileName string, name string) string {

	dockerBuild := exec.Command("docker", "build", "-t", name, "-f", dockerFileName, ".")
	dockerBuild.Dir = name + "/"
	var errb bytes.Buffer
	dockerBuild.Stderr = &errb
	dockerBuild.Stderr = os.Stderr

	_, err := dockerBuild.Output()
	if err != nil {
		// if there was any error, print it here
		log.Println("could not run command: ")

		return "COMPILE ERROR"
	}
	ch := make(chan output)

	go func() {
		cmd := exec.Command("docker", "run", "--rm", name)
		out, err := cmd.CombinedOutput()
		ch <- output{out, err}
	}()

	select {
	case <-time.After(2 * time.Minute):
		log.Println("Should never be here Log and kill all dockres")
	case x := <-ch:

		checkOutputOverflow(&x)

		checkIfCommandFailed(x)

		dockerDeleteImage := exec.Command("docker", "rmi", name)
		dockerDeleteImage.Stderr = os.Stderr

		_, err234 := dockerDeleteImage.Output()
		if err234 != nil {
			// if there was any error, print it here
			log.Println("could not run command: ", err)
		}
		return string(x.out)
	}
	return ""
}

func checkIfCommandFailed(x output) {
	if x.err != nil {
		log.Printf("program errored: %s\n", x.err)
	}
}

func checkOutputOverflow(x *output) {
	if len(string(x.out)) > 1000 {
		x.out = []byte("wouldOverflow")
	}
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
