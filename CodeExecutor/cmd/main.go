package main

import (
	"encoding/json"
	"fmt"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/megamxl/escape-doom/CodeExecutor/internal/constants"
	docker_based "github.com/megamxl/escape-doom/CodeExecutor/internal/engine/docker-based"
	"github.com/megamxl/escape-doom/CodeExecutor/internal/messaging"
	"os"
	"os/signal"
	"syscall"
	"time"
)

func main() {
	if len(os.Args) != 2 {
		fmt.Fprintf(os.Stderr, "Usage: %s <config-file-path>\n",
			os.Args[0])
		os.Exit(1)
	}

	configFile := os.Args[1]
	conf := messaging.ReadKafkaConfig(configFile)
	conf["group.id"] = "kafka-go-getting-started"
	conf["auto.offset.reset"] = "earliest"

	c, err := kafka.NewConsumer(&conf)

	conf = messaging.ReadKafkaConfig(configFile)

	if err != nil {
		fmt.Printf("Failed to create consumer: %s", err)
		os.Exit(1)
	}

	confComplete := messaging.ReadConfig(configFile)

	topicIncoming, exists := confComplete["topic.incoming"]
	if !exists || topicIncoming == "" {
		fmt.Println("Error: 'topic.incoming' is not defined in the configuration")
		os.Exit(1)
	}

	outgoing, exists := confComplete["topic.outgoing"]
	if !exists || topicIncoming == "" {
		fmt.Println("Error: 'topic.outgoing' is not defined in the configuration")
		os.Exit(1)
	}

	// Convert to string if necessary (depending on type in your conf map)
	topic := fmt.Sprintf("%v", topicIncoming)
	fmt.Printf("Subscribed to topic: %s\n", topic)

	// Subscribe to the topic
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
			fmt.Printf("Consumed event from topic %s \n",
				*ev.TopicPartition.Topic)

			var request constants.Request

			err2 := json.Unmarshal(ev.Value, &request)
			if err2 != nil {
				fmt.Println(err2)
			}

			go docker_based.SetupForExecution(&request, conf, outgoing)
		}
	}
	_ = c.Close()
}
