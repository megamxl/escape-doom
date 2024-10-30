
# Requirements

## Introduction

This document is there to define the current state as well as our new goals for turning the current distributed monolith into a clean microservice architecture ready to be deployed and used. To achieve this goal, a few milestones have to be reached.

### The great refactoring
Since the architecture as well as the implementation lack standards and maintainability, it is crucial to take the current state of the project and transform it into a maintainable and readable state. For this task, each team member already has a part of the system to look through and define what has to be changed to get a maintainable code base.

### Microservices
Since the main goal of our group aside from building an useable product is to restructure the current architecture to a true microservice architecture, we have to invest time into this restructuring. Therefore, the team has to learn how to really construct microservices and plan the execution for the next semester.


### Evaluation of new technology
The escape room is a system that is planned to be combined with cutting-edge technology, as well as features that aren't known to be technically feasible in the time given. Therefore, there are technologies that need to be assessed and played around with before being able to decide if they are usable and feasible to implement in the next semester. For a technology to be considered a working prototype or a report, it has to be crafted.


## Glosar
- code executor engine = a module that takes at least a Java file and returns the computed result.



## Systembeschreibung

## Erledigte Anforderung

## Geplante Anforderungen (1. Sem.)

### Refactoring

- #### Gamesession

    - The module must include automated tests with at least 60% code coverage before refactoring.
  
    - The system must be refactored according to [Java and Maven multi-module](https://vaadin.com/docs/latest/building-apps/project-structure/multi-module) / [Baelding](https://www.baeldung.com/maven-multi-module) standards.

- #### LectorPortal

    - The module must include automated tests with at least 60% code coverage before refactoring.

    - The system must be refactored according to [Java and Maven multi-module](https://vaadin.com/docs/latest/building-apps/project-structure/multi-module) / [Baelding](https://www.baeldung.com/maven-multi-module) standards.

- #### CodeExecutor

    - The system must be refactored according to the [go standard](https://github.com/golang-standards/project-layout)

    - The system must realize the Kafka connection via a configuration which is loaded at runtime

    - The system should have the modularity that the code executor engine is exchangable if a new implemntaion matches the interface

    - The system should have a test code coverage of 60 % over all files containing some kind of logic.

    - The system could provide the ability to configure the used code executor engine with the same configuration as the Kafka properties

    <br>

    - The system won't introduce a new code executor engine

    - The system won't change the current behavior

    - The system won't provide multi-file execution behavior

- #### Frontend

    - The system must be refactored to meet the [next.js standards](https://github.com/dwarvesf/nextjs-boilerplate/blob/master/docs/CODE_STYLE.md)
    - The systems 3 most important features must be tested using E2E tests
    - The system must prepare the code-structure to switch from SSE to WS
 
    <br>

    - The system should be modular enough that new features can be added without refactoring the whole code
    - The system should improve the placement of InteractionNodes to handle page resizing
  
    <br>

    - The system could utilize Next.js features to improve data fetching latency using Server Components
    - The system could improve the integration of TanStack Query

    <br>

    - The system won't split the frontend into multiple packages this semester

- ## Microservices

    - The system must be documented and planned according to the [c4 Standard](https://c4model.com/), with a focus on the 
      microservice section of this standard
    - The system must be planned technology wise to define which technology's will be used to implement the microservice architecture
    - The system should provide a planned deployment structure for kubernetes 
    - The services that our System relies on, such as PostgreSQL, Redis, Kafka and all others we document should be preemptively 
      deployed as Helm charts
    - The spring cloud ecosystem could be evaluated for the microservice transition the system is going to do

- ## Evaluierung Neuer Technoliegen

    - The system should be evaluated how parallel access by team members (two window tabs or access to the same room on two PCs)
    - The system should be evaluated to integrate an editor with multifiles (tabs multiple classes/interfaces), code completions for jdk functions)
    - The system should be evaluated to enable a student login to allow cross device session and state tracking
    - The system should be evaluated on how to provide a streamlined process for Escape room creation
    - The system should be evaluated  different ways to compile and run use code safely. Non-binding ideas (Wasm, Open-Source Serverless Function Framework, Docker, Podman?)
