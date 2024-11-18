## What is the Executor

## Configuration

## Build the docker container

In the root directory (where this main file is) execute this command

```bash
docker build -t executor -f deployments/app.Dockerfile .   
````

### To run it you need to add it to the dev compose or run it with this command.

Never forget to mount the docker socket 

```bash
docker run -v /var/run/docker.sock:/var/run/docker.sock executor 
```