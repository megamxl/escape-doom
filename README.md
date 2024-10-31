# Escape Doom

## Components

- CodeExecutor: Responsible for handling code submissions, compilation, and execution within the escape room environment.
- GameSession: Manages game logic, user sessions, and communication with the CodeExecutor.
- LectorPortal: provides endpoints and interface for lecturers to create, manage, and monitor escape room sessions.
- Frontend: A React-based UI for students and lecturers, facilitating interaction with the game and its components.

>TO DO: create and link dedicated README.md in each component

## Prerequisites

...


## Set-Up

- ...
- Navigate to the EscapeDoom/docker folder.
- Run the following command:
`docker compose -f ./docker-compose-dev.yaml up`
- Start the Game Session Spring application
- Then, start the Lector Portal Spring application
- Note: Start the Game Session before the Lector Portal, as both use ddl:create drop and share tables.
- Set up and Start the Frontend:
`npm install`
`npm run dev`


## Usage

Lecturer:

- Open the frontend in your browser at http://localhost:80/login (or the designated port if configured differently).
- Use the default login credentials:
- Email: leon@escapeddoom.com
- Password: escapeDoom

Note: Ensure that the Game Session service is running before launching sessions.

Students:
- Students can join an active escape room session using a session code provided by the lecturer.
Within the game, students solve riddles and submit code through the integrated editor.
The CodeExecutor processes submissions and returns results in real-time for students.

...

## Configuration


Backend: application.yml or .env files for environment variables.

Docker: docker-compose-deploy.yaml

Frontend:  package.json or tsconfig.json

### Code Executor

...

### Game Session

| Category               | Setting                         | Value                                                |
|------------------------|---------------------------------|------------------------------------------------------|
| **Base URLs**          | VITE_LECTOR_BASE_URL            | `http://localhost:8080/api/v1`                       |
|                        | VITE_GAME_BASE_URL              | `http://localhost:8090/api`                          |
| **Spring Kafka**       | Bootstrap Servers               | `localhost:9092`                                     |
|                        | Consumer Group ID               | `computedCode`                                       |
|                        | Code Compiler Topic              | `codeCompiler` (Kafka topic for code execution)      |
| **Spring MVC**         | Asynchronous Request Timeout    | No timeout (`-1`)                                    |
| **Spring Session**     | Session Store                   | Redis (`spring:session` namespace)                   |
|                        | Session Timeout                 | `12 hours`                                           |
| **Data Source**        | Database URL                    | `jdbc:postgresql://localhost:5433/LectorPortal`      |
| (PostgreSQL)           | Username                        | `myuser`                                             |
|                        | Password                        | `mypassword`                                         |
| **Connection Pooling** | Minimum Idle Connections        | `10`                                                 |
| (HikariCP)             | Maximum Pool Size               | `50`                                                 |
|                        | Idle Timeout                    | `1200ms`                                             |
|                        | Connection Timeout              | `3000ms`                                             |
| **JPA and Hibernate**  | DDL-Auto                        | `validate` (validates schema without altering)       |
|                        | Show SQL                        | Enabled (`true`)                                     |
|                        | Dialect                         | PostgreSQL (`PostgreSQLDialect`)                     |
| **Redis Data Store**   | Host                            | `localhost`                                          |
|                        | Port                            | `6379`                                               |
| **Server**             | Port                            | `8090`                                               |

### Lector Portal

...



## Deployment

Either build all Dockerfiles manually and execute the compose deployment.
...

## Contributing

Rules
...