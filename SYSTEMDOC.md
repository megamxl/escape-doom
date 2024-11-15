# System Documentation

---

- [Introduction](#introduction)
- [System Description](#system-description)
- [Projected Overhaul](#projected-overhaul)
- [Glossary](#glossary)
- [Requirements Refactoring](#requirements-refactoring)
  - [GameSession](#gamesession)
  - [LectorPortal](#lectorportal)
  - [CodeExecutor](#codeexecutor)
  - [Frontend](#frontend)
- [Requirements Microservices](#requirements-microservices)
- [Requirements Evaluation of New Technologies](#requirements-evaluation-of-new-technologies)

---
## Introduction

This document is there to define the current state as well as our new goals for turning the current distributed monolith into a clean microservice architecture ready to be deployed and used. To achieve this goal, a few milestones have to be reached.

## System Description

Escape Doom is a prototype for a web-based escape room designed to test the feasibility and technological requirements for creating such experiences. It includes a Java backend and a React frontend, featuring multiple components like the Game Session, Lector Portal, and Executor Service.

![Visual Abstract](https://hackmd.io/_uploads/rJ9L0oeb1g.png)

The initial scope includes a single pre-configured escape room, with plans to evolve into a customizable tool. Students participate without the need for an account, while logged-in lecturers manage and control the escape room sessions.

This document aims to define the current state and establish new goals to transition the application from a distributed monolithic prototype into a clean microservice architecture.

---

## Projected Overhaul

### 1st Semester

#### Initial Refactoring: 

Since the architecture as well as the implementation lack standards and maintainability, it is crucial to take the current state of the project and transform it into a maintainable and readable state. For this task, each team member already has a part of the system to look through and define what has to be changed to get a maintainable code base.
- Key Actions:
  - Decouple existing components to increase modularity.
  - Eliminate in-memory state.
  - Replace bad technology choices 

#### Requirements Analysis / Microservices:
Since the main goal of our group aside from building a usable product is to restructure the current architecture to a true microservice architecture, we have to invest time into this restructuring. Therefore, the team has to learn how to really construct microservices and plan the execution for the next semester.
- Key Actions:
  - Conduct a detailed analysis of the requirements necessary for transitioning from a monolithic to a microservice architecture.
  - Identify possible approaches, patterns, and best practices tailored to our needs.
  - Talk to leon to and evaluated feedback recursively

#### Evaluate New Technologies:
The escape room is a system that is planned to be combined with cutting-edge technology, as well as features that aren't known to be technically feasible in the time given. Therefore, there are technologies that need to be assessed and played around with before being able to decide if they are usable and feasible to implement in the next semester. For a technology to be considered as working a prototype or a report / presentation needs to be built.
- Key Actions:
  - Explore, evaluate and select new technologies.
  - Provide a holistic overview of the future of our product 

### 2nd Semester visions

#### Microservice Development and Testing:
  - Develop the individual microservices based on the identified requirements and patterns.
  - Test the new modular structure to ensure that each microservice operates independently and integrates effectively within the overall system.
  - Deployment documentation

#### Handover Preparation:
  - Finalize the fully refactored and modularized microservice architecture.
  - Finalize documentation to ensure that the product is maintainable.
  - Finalize arc42 architecture documentation.

---

## Glossary

| Term                 | Definition                                                                                                                   | 
|----------------------|------------------------------------------------------------------------------------------------------------------------------|
| CodeExecutor         | Component manages game logic and user sessions, facilitating communication with the CodeExecutor for real-time interactions. |
| Code Executor Engine | A module that takes a (Java) file as input and returns the computed result.                                                  |
| Game Session         | Component that provides a user interface for lecturers to manage and monitor escape room sessions effectively.               |
| Lector Portal        | Component that that provides endpoints for lecturers to create, manage, and monitor escape room sessions.                    |

## Requirements Refactoring

### GameSession

| ID    | Requirement                                                                                                                                                      | Fulfilled | Responsible Developer |
|-------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|-----------------------|
|       | Must                                                                                                                                                             |           |                       |
| GS-01 | PlayerStateManagementService must be split into smaller, specialized Java classes (such as LobbyService and possibly CodeCompilingService).                      |           | @Jaquie @Mark         |
| GS-02 | The system must transition from using an in-memory sseEmitters list to WebSockets for real-time bidirectional communication.                                     |           | @Jaquie               |
| GS-03 | All runtime errors must be logged and handled with structured error management throughout the system to address both expected and unexpected issues.             |           | @Mark                 |
| GS-04 | The module must include automated tests with at least 60% code coverage.                                                                                         |           | @Mark                 |
| GS-05 | The system must be refactored according to [Java and Maven multi-module standards](https://vaadin.com/docs/latest/building-apps/project-structure/multi-module). |           | @Mark                 |
| GS-06 | User-facing error messages must be descriptive, enabling users to understand and respond to errors effectively.                                                  |           | @Anas                 |
|       | Should                                                                                                                                                           |           |                       |
| GS-07 | Error messages should be categorized at appropriate levels (info, warning, error).                                                                               |           | @Jacquie              |

### LectorPortal

| ID    | Requirement                                                                                                                                                      | Fulfilled | Responsible Developer |
|-------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|-----------------------|
|       | Must                                                                                                                                                             |           |                       |
| LP-01 | The module must include automated tests with at least 60% code coverage.                                                                                         |           | @Mark                 |
| LP-02 | The system must be refactored according to [Java and Maven multi-module standards](https://vaadin.com/docs/latest/building-apps/project-structure/multi-module). |           | @Mark                 | 
| LP-03 | All runtime errors must be logged and handled with structured error management throughout the system to address both expected and unexpected issues.             |           | @Jaquie               |
| LP-04 | User-facing error messages must be descriptive, enabling users to understand and respond to errors effectively.                                                  |           | @Anas                 |
|       | Should                                                                                                                                                           |           |                       |
| LP-05 | Error messages should be categorized at appropriate levels (info, warning, error).                                                                               |           | @Jacquie              |

### CodeExecutor

| ID    | Requirement                                                                                                                                  | Fulfilled | Responsible Developer |
|-------|----------------------------------------------------------------------------------------------------------------------------------------------|-----------|-----------------------|
|       | Must                                                                                                                                         |           |                       |
| CE-01 | The system must be refactored according to the [Go project layout standard](https://github.com/golang-standards/project-layout).             |           | @Maxl                 |
| CE-02 | The system must establish the Kafka connection through a runtime-loaded configuration.                                                       |           | @Maxl                 |
| CE-03 | The system should be modular enough to allow the code executor engine to be replaceable, if the new implementation adheres to the interface. |           | @Maxl                 |
|       | Should                                                                                                                                       |           |                       |
| CE-04 | The system should have a test coverage of at least 60% across all logic-containing files.                                                    |           | @Maxl @Thommy         |
|       | Could                                                                                                                                        |           |                       |
| CE-05 | The system could allow configuration of the code executor engine using the same properties configuration as Kafka.                           |           | @Anas                 |
|       | Wont                                                                                                                                         |           |                       |
| CE-06 | The system won't introduce a new code executor engine.                                                                                       |           |                       |
| CE-07 | The system won't provide multi-file execution behavior.                                                                                      |           |                       |

### Frontend

| ID    | Requirement                                                                                                                                   | Fulfilled | Responsible Developer |
|-------|-----------------------------------------------------------------------------------------------------------------------------------------------|-----------|-----------------------|
|       | Must                                                                                                                                          |           |                       |
| FE-01 | The system must be refactored to adhere to [Next.js standards](https://github.com/dwarvesf/nextjs-boilerplate/blob/master/docs/CODE_STYLE.md). |           | @Thommy               |
| FE-02 | The system must be migrated from pure React to Next.js?                                                                                       |           | @Thommy               |
| FE-03 | The system must provide end-to-end (E2E) testing solution.                                                                                    |           | @Thommy               |
| FE-04 | The system must prepare the code structure to support a transition from SSE (Server-Sent Events) to WebSockets (WS).                          |           | @Jaquie               |
|       | Should                                                                                                                                        |           |                       |
| FE-05 | The system source code should be structured that all files for the lector portal frontend are in one folder.                                  |           | @Anas                 |
|       | Could                                                                                                                                         |           |                       |
| FE-06 | The system could leverage Next.js features to improve data fetching latency via Server Components.                                            |           | @Thommy               |
| FE-07 | The system could enhance integration with TanStack Query.                                                                                     |           | @Anas                 |
|       | Won't                                                                                                                                         |           |                       |
| FE-08 | The system won't split the frontend into multiple packages this semester.                                                                     |           |                       |

## Requirements Microservices

| ID    | Requirement                                                                                                                         | Fulfilled | Responsible Developer |
|-------|-------------------------------------------------------------------------------------------------------------------------------------|-----------|-----------------------|
|       | Must                                                                                                                                |           |                       |
| MS-01 | The system must be documented and planned following the [C4 model](https://c4model.com/), with emphasis on microservice components. |           |                       |
| MS-02 | Technology choices for implementing the microservice architecture must be defined and documented.                                   |           |                       |
|       | Should                                                                                                                              |           |                       |
| MS-03 | The developer team should provide a plan of the Kubernetes deployment structure.                                                    |           |                       |
| MS-04 | System dependencies (e.g., PostgreSQL, Redis, Kafka) should be preemptively deployed on Kubernetes.                                 |           |                       |
|       | Could                                                                                                                               |           |                       |
| MS-05 | The Spring Cloud ecosystem could be evaluated for its suitability in the microservice transition.                                   |           |                       |

## Requirements Evaluation of New Technologies

| ID    | Requirement                                                                                                                                                                                                                                                                | Fulfilled | Responsible Developer |
|-------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|-----------------------|
|       | Should                                                                                                                                                                                                                                                                     |           |                       |
| NT-01 | The system should be evaluated for handling parallel access (e.g., two browser tabs or different PC accessing the same room).                                                                                                                                              |           |                       |
| NT-02 | The system should be evaluated for potential multi-file support in the code editor (e.g., multiple tabs for classes/interfaces) and code completion for JDK functions.                                                                                                     |           |                       |
| NT-03 | The system should be evaluated for adding student login capabilities to enable cross-device session tracking.                                                                                                                                                              |           |                       |
| NT-04 | The system should be evaluated to streamline the escape room creation process.                                                                                                                                                                                             |           |                       |
|       | Could                                                                                                                                                                                                                                                                      |           |                       |
| NT-05 | The system could be evaluated for safe user code compilation and execution approaches. Non-binding ideas include using WASM, open-source serverless function frameworks, Docker, or Podman.                                                                                |           |                       |
| NT-06 | The use of caching technologies, such as Redis, should be evaluated to improve access times for frequently accessed data.                                                                                                                                                  |           |                       |
| NT-07 | The system could be evaluated for tracking key game events in the frontend, such as time on stages, necessary hint requests, and incorrect attempts, and sending them to the server for analytics (non-binding ideas: custom dashboards or tools like Grafana, ELK stack). |           |                       |
| NT-08 | New technologies could be evaluated to enhance the placement of interaction nodes in the frontend, supporting responsive resizing.                                                                                                                                         |           |                       | 

- The escape room system is planned to incorporate cutting-edge technology, as well as features that may not be technically feasible within the given timeframe.
- Therefore, certain technologies need to be assessed and experimented with before deciding if they are usable and feasible to implement in the next semester.
- For a technology to be considered, a working prototype must be developed and/or a report of the evaluation must be provided.