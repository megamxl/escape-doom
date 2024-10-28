
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

- #### LectorPortal

- #### CodeExecutor

    - The system must be refactored according to the [go standard](https://github.com/golang-standards/project-layout)

    - The system must realize the Kafka connection via a configuration which is loaded at runtime

    - The system should have the modularity that the code executor engine is easily replaceable

    - The system should have a test code coverage of 60 % over all files containing some kind of logic.

    - The system could provide the ability to configure the used code executor engine with the same configuration as the Kafka properties

    <br>

    - The system won't introduce a new code executor engine

    - The system won't change the current behavior

    - The system won't provide multi-file execution behavior

- #### Frontend

### Microservices

### Evaluierung Neuer Technoliegen
