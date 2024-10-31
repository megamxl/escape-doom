# System Documentation

---

- [System Description](#system-description)
- [Projected Overhaul](#planned-overhaul)
- [Glossary](#glossary)
- [Requirements Refactoring](#requirements-refactoring)
  - [GameSession](#gamesession)
  - [LectorPortal](#lectorportal)
  - [CodeExecutor](#codeexecutor)
  - [Frontend](#frontend)
- [Requirements Microservices](#requirements-microservices)
- [Requirements Evaluation of New Technologies](#evaluation-of-new-technologies)

---

## System Description

Escape Doom is a prototype for a web-based escape room designed to test the feasibility and technological requirements for creating such experiences. It includes a Java backend and a React frontend, featuring multiple components like the Game Session, Lector Portal, and Executor Service.

![Visual Abstract](https://hackmd.io/_uploads/rJ9L0oeb1g.png)

The initial scope includes a single pre-configured escape room, with plans to evolve into a customizable tool. Students participate without the need for an account, while logged-in lecturers manage and control the escape room sessions.

This document aims to define the current state and establish new goals to transition the application from a distributed monolithic prototype into a clean microservice architecture.

---

## Projected Overhaul

### 1st Semester

#### Initial Refactoring:
  - Decouple existing components to increase modularity.
  - Eliminate in-memory state.
  - ..

#### Requirements Analysis:
  - Conduct a detailed analysis of the requirements necessary for transitioning from a monolithic to a microservice architecture.
  - Identify possible approaches, patterns, and best practices tailored to our needs.
  - Explore, evaluate and select new technologies.
  - ..

### 2nd Semester

#### Microservice Development and Testing:
  - Develop the individual microservices based on the identified requirements and patterns.
  - Test the new modular structure to ensure that each microservice operates independently and integrates effectively within the overall system.
  - ..

#### Handover Preparation:
  - Finalize the fully refactored and modularized microservice architecture.
  - Finalize documentation to ensure that the product is maintainable.
  - ..

---

## Glossary

| Term                 | Definition                                                                                                                   | 
|----------------------|------------------------------------------------------------------------------------------------------------------------------|
| CodeExecutor         | Component manages game logic and user sessions, facilitating communication with the CodeExecutor for real-time interactions. |
| Code Executor Engine | A module that takes a (Java) file as input and returns the computed result.                                                  |
| Game Session         | Component that provides a user interface for lecturers to manage and monitor escape room sessions effectively.               |
| Lector Portal        | Component that that provides endpoints for lecturers to create, manage, and monitor escape room sessions.                                                                                                               |

## Requirements Refactoring

### GameSession

| ID     | Requirement                                                                                                                                                     | Fulfilled | Responsible Developer |
|--------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|------------------------|
| GS-01  | PlayerStateManagementService must be split into smaller, specialized services (such as LobbyService and possibly CodeCompilingService).                         |           |                        |
| GS-02  | Each new service must follow a single-responsibility design.                                                                                                    |           |                        |
| GS-03  | The system must transition from using an in-memory sseEmitters list to WebSockets for real-time bidirectional communication.                                    |           |                        |
| GS-04  | Critical methods should include logging statements that capture method entry, exit, and error details.                                                          |           |                        |
| GS-05  | Structured error handling should be applied throughout the services to address both expected and unexpected issues.                                             |           |                        |
| GS-06  | Error messages should be categorized at appropriate levels (info, warning, error).                                                                              |           |                        |
| GS-07  | User-facing error messages must be descriptive, enabling users to understand and respond to errors effectively.                                                 |           |                        |
| GS-08  | Frequently accessed data could be cached (Redis) for faster access.                                                                                             |           |                        |
| GS-09  | The module must include automated tests with at least 60% code coverage before refactoring.                                                                     |           |                        |
| GS-10  | The system must be refactored according to [Java and Maven multi-module standards](https://vaadin.com/docs/latest/building-apps/project-structure/multi-module). |           |                        |

### LectorPortal

| ID     | Requirement                                                                                                                                                   | Fulfilled | Responsible Developer |
|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|------------------------|
| LP-01  | The module must include automated tests with at least 60% code coverage before refactoring.                                                                   |           |                        |
| LP-02  | The system must be refactored according to [Java and Maven multi-module standards](https://vaadin.com/docs/latest/building-apps/project-structure/multi-module). |           |                        |

### CodeExecutor

| ID     | Requirement                                                                                                                                        | Fulfilled | Responsible Developer |
|--------|----------------------------------------------------------------------------------------------------------------------------------------------------|-----------|------------------------|
| CE-01  | The system must be refactored according to the [Go project layout standard](https://github.com/golang-standards/project-layout).                   |           |                        |
| CE-02  | The system must establish the Kafka connection through a runtime-loaded configuration.                                                             |           |                        |
| CE-03  | The system should be modular enough to allow the code executor engine to be replaceable, provided the new implementation adheres to the interface. |           |                        |
| CE-04  | The system should maintain test coverage of at least 60% across all logic-containing files.                                                        |           |                        |
| CE-05  | The system could allow configuration of the code executor engine using the same properties configuration as Kafka.                                 |           |                        |
| CE-06  | The system won't introduce a new code executor engine.                                                                                             |           |                        |
| CE-07  | The system will not alter its original behavior. (behavior refers to?)                                                                             |           |                        |
| CE-08  | The system won't provide multi-file execution behavior.                                                                                            |           |                        |

### Frontend

| ID     | Requirement                                                                                                                                                   | Fulfilled | Responsible Developer |
|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|------------------------|
| FE-01  | The system must be refactored to adhere to [Next.js standards](https://github.com/dwarvesf/nextjs-boilerplate/blob/master/docs/CODE_STYLE.md).                |           |                        |
| FE-02  | The system’s three most critical features must be tested using end-to-end (E2E) tests.                                                                        |           |                        |
| FE-03  | The system must prepare the code structure to support a transition from SSE (Server-Sent Events) to WebSockets (WS).                                          |           |                        |
| FE-04  | The system should be modular enough to add new features without requiring codebase refactoring.                                                               |           |                        |
| FE-05  | The system should enhance the placement of interaction nodes to support responsive resizing.                                                                   |           |                        |
| FE-06  | The system could leverage Next.js features to improve data fetching latency via Server Components.                                                            |           |                        |
| FE-07  | The system could enhance integration with TanStack Query.                                                                                                     |           |                        |

## Requirements Microservices

| ID     | Requirement                                                                                                                                                                                                                                       | Fulfilled | Responsible Developer |
|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|------------------------|
| MS-01  | The system must be documented and planned following the [C4 model](https://c4model.com/), with emphasis on microservice components. (not sure about this as göschka wanted us to describe architecture in arc 42, we also learn about it in LV..) |           |                        |
| MS-02  | The system must define technology choices for implementing the microservice architecture.                                                                                                                                                         |           |                        |
| MS-03  | The system should provide a planned Kubernetes deployment structure.                                                                                                                                                                              |           |                        |
| MS-04  | System dependencies (e.g., PostgreSQL, Redis, Kafka) should be preemptively deployed as Helm charts.                                                                                                                                              |           |                        |
| MS-05  | The Spring Cloud ecosystem could be evaluated for its suitability in the microservice transition.                                                                                                                                                 |           |                        |

## Requirements Evaluation of New Technologies

| ID     | Requirement                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Fulfilled | Responsible Developer |
|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|------------------------|
| NT-01  | The system should be evaluated for handling parallel access (e.g., two browser tabs or different PC accessing the same room).                                                                                                                                                                                                                                                                                                                                                   |           |                        |
| NT-02  | The system should be evaluated for potential multi-file support in the code editor (e.g., multiple tabs for classes/interfaces) and code completion for JDK functions.                                                                                                                                                                                                                                                                                                          |           |                        |
| NT-03  | The system should be evaluated for adding student login capabilities to enable cross-device session and state tracking.                                                                                                                                                                                                                                                                                                                                                         |           |                        |
| NT-04  | The system should be evaluated to streamline the escape room creation process.                                                                                                                                                                                                                                                                                                                                                                                                  |           |                        |
| NT-05  | The system should be evaluated for safe user code compilation and execution approaches. Non-binding ideas include using WASM, open-source serverless function frameworks, Docker, or Podman.                                                                                                                                                                                                                                                                                    |           |                        |
| NT-06  | The system could be evaluated for tracking key game events in the frontend, such as time on stages, necessary hint requests, and incorrect attempts, and sending them to the server via WebSockets. This evaluation includes assessing existing technologies, Redis for temporary storage, Kafka for batching and aggregation, and ultimately storing aggregated data in a database, enabling data access for analytics via custom dashboards or tools like Grafana, ELK stack. |           |                        |
