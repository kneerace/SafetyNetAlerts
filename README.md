# SafetyNetAlerts
Emergency Alert and Notification Service

## Project Description

SafetyNet Alerts is a Spring Boot application designed to provide critical emergency information to first responders. It aggregates and delivers data about individuals and their locations to aid in effective response during emergencies such as fires, severe weather, and floods.

The application's core functionality includes:

* Providing information about residents near emergency events.
* Listing people serviced by specific fire stations.
* Delivering resident contact information for emergency notifications.
* Supplying detailed medical information for vulnerable individuals.

By providing timely and accurate information, SafetyNet Alerts aims to enhance the preparedness and effectiveness of emergency response teams, ultimately saving lives.

## Features

The application provides the following key features through its API endpoints:

* **Fire Station Information:** Retrieves a list of people serviced by a given fire station, including personal details and a summary of adults and children.
* **Child Alert:** Lists children residing at a specific address, along with other household members.
* **Phone Alert:** Provides a list of phone numbers for individuals within a fire station's jurisdiction.
* **Fire Incident Information:** Returns the fire station servicing an address, along with detailed information about residents at that address, including medical records.
* **Flood Alert:** Lists households within specified fire station jurisdictions, grouping people by address and including personal and medical information.
* **Person Information:** Retrieves comprehensive information about a person, including contact details, age, and medical history.
* **Community Email:** Provides email addresses for all residents in a given city.
* **Data Management:** Functionality to add, update, and delete person records, fire station mappings, and medical records.

## Endpoints

###   Data Retrieval Endpoints

* `GET /firestation?stationNumber=<station_number>`
* `GET /childAlert?address=<address>`
* `GET /phoneAlert?firestation=<firestation_number>`
* `GET /fire?address=<address>`
* `GET /flood/stations?stations=<a list of station_numbers>`
* `GET /personInfo?firstName=<firstName>&lastName=<lastName>`
* `GET /communityEmail?city=<city>`

###   Data Management Endpoints

* `POST /person` (Add a new person)
* `PUT /person` (Update an existing person)
* `DELETE /person` (Delete a person)
* `POST /firestation` (Add a fire station/address mapping)
* `PUT /firestation` (Update an address' fire station number)
* `DELETE /firestation` (Delete a fire station/address mapping)
* `POST /medicalRecord` (Add a medical record)
* `PUT /medicalRecord` (Update an existing medical record)
* `DELETE /medicalRecord` (Delete a medical record)

###   Spring Boot Actuator Endpoints

* `GET /actuator/health` (Application health status)
* `GET /actuator/info` (Application information)
* `GET /actuator/metrics` (Application metrics)
* `GET /actuator/trace` (Request tracing)

## Architecture

The application is designed using the **Model-View-Controller (MVC)** pattern and adheres to the **SOLID** principles. This architecture promotes code maintainability, scalability, and testability.

## Technologies Used

* **Java:** Programming language
* **Spring Boot:** Framework for building the application

## Getting Started

###   Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven or Gradle (for dependency management and building)
* Git (for version control)

###   Installation

1.  Clone the repository:
    ```bash
    git clone https://github.com/kneerace/SafetyNetAlerts.git
    ```
2.  Navigate to the project directory:
    ```bash
    cd SafetyNetAlerts
    ```
3.  Build the application using Maven:
    ```bash
    mvn clean install
    ```

###   Running the Application

1.  Run the Spring Boot application:
    ```bash
    java -jar target/SafetyNetAlerts-0.0.1-SNAPSHOT.jar
    ```
   
2.  The application will be accessible at `http://localhost:8080`.

## Unit Tests

The application includes a suite of unit tests following the testing pyramid paradigm to ensure code quality and functionality.

* tests are located under "src/test/java/..."
* To run the tests using Maven:
    ```bash
    mvn test
    ```

## Logging

The application logs all requests and responses to provide traceability and aid in debugging.

## Domain Model

```mermaid
classDiagram
    class Person {
        -String firstName
        -String lastName
        -String address
        -String city
        -String zip
        -String phone
        -String email
    }

    class FireStation {
        -String station
        -String address
    }

    class MedicalRecord {
        -String firstName
        -String lastName
        -String birthdate
        -List<String> medications
        -List<String> allergies
    }

    Person "1" -- "1" MedicalRecord : has
    FireStation "1" -- "*" Person : services
