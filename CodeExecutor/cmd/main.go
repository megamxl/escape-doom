package main

import (
	"encoding/json"
	"fmt"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/megamxl/escape-doom/CodeExecutor/internal/constants"
	docker_based "github.com/megamxl/escape-doom/CodeExecutor/internal/engine/docker-based"
	no_op "github.com/megamxl/escape-doom/CodeExecutor/internal/engine/no-op"
	"github.com/megamxl/escape-doom/CodeExecutor/internal/messaging"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"
)

func main() {
	if len(os.Args) != 2 {
		log.Fatalf("Usage: %s <config-file-path>\n",
			os.Args[0])
	}

	configFile := os.Args[1]
	conf := messaging.ReadKafkaConfig(configFile)
	conf["group.id"] = "kafka-go-getting-started"
	conf["auto.offset.reset"] = "earliest"

	c, err := kafka.NewConsumer(&conf)

	conf = messaging.ReadKafkaConfig(configFile)

	if err != nil {
		log.Printf("Failed to create consumer: %s", err)
		os.Exit(1)
	}

	confComplete := messaging.ReadConfig(configFile)

	topicIncoming, exists := confComplete["topic.incoming"]
	if !exists || topicIncoming == "" {
		log.Println("Error: 'topic.incoming' is not defined in the configuration")
		os.Exit(1)
	}

	outgoing, exists := confComplete["topic.outgoing"]
	if !exists || topicIncoming == "" {
		log.Println("Error: 'topic.outgoing' is not defined in the configuration")
		os.Exit(1)
	}

	// Convert to string if necessary (depending on type in your conf map)
	topic := fmt.Sprintf("%v", topicIncoming)
	log.Printf("Subscribed to topic: %s\n", topic)

	// Subscribe to the topic
	err = c.SubscribeTopics([]string{topic}, nil)

	// Set up a channel for handling Ctrl-C, etc
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

	engine, err2 := createEngineBasedOnConfig(confComplete["engine"])
	if err2 != nil {
		log.Fatalf("Failed to create engine: %s", err2)
	}

	// Process messages
	run := true
	for run {
		select {
		case sig := <-sigchan:
			log.Printf("Caught signal %v: terminating\n", sig)
			run = false
		default:
			ev, err := c.ReadMessage(100 * time.Millisecond)
			if err != nil {
				// Errors are informational and automatically handled by the consumer
				continue
			}

			var request constants.Request

			err2 := json.Unmarshal(ev.Value, &request)
			if err2 != nil {
				log.Println(err2)
			}

			go runCodeAndSendResponse(request, conf, outgoing, engine)
		}
	}
	_ = c.Close()
}

func runCodeAndSendResponse(request constants.Request, conf kafka.ConfigMap, outgoing string, engine constants.Engine) {

	log.Printf("compling code for user %s", request.PlayerSessionId)

	result := engine.ExecuteCode(&request)
	messaging.SendMessage(outgoing, conf, &request, result)
}

func createEngineBasedOnConfig(engineName string) (constants.Engine, error) {

	if engineName == "docker" {
		return docker_based.DockerEngine{}, nil
	} else if engineName == "no-op" {
		return no_op.NoOpExecutor{}, nil
	} else {
		return nil, fmt.Errorf("Unknown engine '%s'", engineName)
	}

}
