package main

import (
	"encoding/json"
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

type Request struct {
	PlayerSessionId string `json:playerSessionId`
	Language        string `json:language`
	Code            string `json:code`
	DateTime        string `json:dateTime`
}

func main() {
	fmt.Println("hello")

	if len(os.Args) != 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <config-file-path>\n",
			os.Args[0])
		os.Exit(1)
	}

	configFile := os.Args[1]
	conf := ReadConfig(configFile)
	conf["group.id"] = "kafka-go-getting-started"
	conf["auto.offset.reset"] = "earliest"

	c, err := kafka.NewConsumer(&conf)

	if err != nil {
		fmt.Printf("Failed to create consumer: %s", err)
		os.Exit(1)
	}

	//TODO Change for real thing
	topic := "codeCompiler"
	err = c.SubscribeTopics([]string{topic}, nil)
	// Set up a channel for handling Ctrl-C, etc
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

	// Process messages
	run := true
	for run {
		select {
		case sig := <-sigchan:
			fmt.Printf("Caught signal %v: terminating\n", sig)
			run = false
		default:
			ev, err := c.ReadMessage(100 * time.Millisecond)
			if err != nil {
				// Errors are informational and automatically handled by the consumer
				continue
			}
			fmt.Printf("Consumed event from topic %s value = %s\n",
				*ev.TopicPartition.Topic, string(ev.Value))

			var request Request

			err2 := json.Unmarshal(ev.Value, &request)
			if err2 != nil {
				fmt.Println(err2)
			}
			fmt.Println("the request is", request.Code)

			go setupForExecution(&request, conf)
		}
	}
	c.Close()
}
