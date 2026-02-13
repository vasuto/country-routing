# Country Routing Service
## Overview

This is a Spring Boot application that provides a service to calculate the shortest land route between two countries. The application fetches data about countries, including their borders, from an external API and finds the shortest path using the Breadth-First Search (BFS) algorithm.

## Features

Exposes a REST endpoint to calculate a route between two countries.
Uses BFS to find the shortest path between two countries.
Supports fetching country data (including borders) from an external API.
The application works with country cca3 codes instead of full country names.

## Technologies

- Spring Boot (for building the REST service)
- Spring WebFlux (for handling HTTP requests asynchronously using WebClient)
- Jackson (for parsing JSON data)
- JUnit 5 (for testing)

## Setup and Installation
Requirements

- JDK 17 or higher
- Maven

## Online demo

Online demo is availale on https://country-routing.onrender.com
```
https://country-routing.onrender.com/routing/CZE/ITA
```

## Steps to Run the Application


### Build the project:
```
mvn clean install
```

### Run the application:
```
mvn spring-boot:run
```

The application will be available at http://localhost:8080.

### Configuration

You can configure the external API URL in src/main/resources/application.yml:
```
country:
  api:
    url: https://raw.githubusercontent.com/mledoze/countries/master/countries.json
```

This configuration tells the application where to fetch the country data.

### API Endpoint
```
GET /routing/{origin}/{destination}
```
Parameters:

origin: The cca3 code of the starting country (e.g., "CZE" for the Czech Republic).

destination: The cca3 code of the destination country (e.g., "DEU" for Germany).

Response:

A JSON object with the route.

Example for Czech Republic to Germany:
```
{
  "route": ["CZE", "DEU"]
}
```
Postman collection for trying out is available in the sourcecode.

### Error Handling:

If there is no possible route between the origin and destination, the service returns a 400 Bad Request error.

If invalid country codes are provided, the service returns an empty list.

Example Request

To find the route from the Czech Republic (CZE) to Italy (ITA):
```
curl -X GET "http://localhost:8080/routing/CZE/ITA"
```

The response will be:
```
{
  "route": ["CZE", "AT", "ITA"]
}
```
### Running Tests

To run the tests:
```
mvn test
```