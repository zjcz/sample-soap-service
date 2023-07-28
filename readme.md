# Sample Soap Service - Movie API

The Movie API is a sample SOAP service written in Java Spring Boot that provides basic CRUD (Create, Read, Update, Delete) functionality to manage a list of movie entities. This API allows users to interact with movie data through SOAP-based endpoints. 

I created this project to provide a SOAP service I could use while experimenting with a number of technologies including Linux, Tomcat, AWS and SOAP UI. I also wanted to create a simple API that I could use as a reference for future projects.

The project uses the following technologies:
- Java 17
- Spring Boot 3.1.1
- Maven
- H2 Database

For a sample REST web service, see the [Sample REST Service - Movie API](https://www.github.com/zjcz/sample-rest-service) project.

## Setup

To get started, clone the repository and navigate to the project directory

```bash
git clone https://github.com/zjcz/sample-soap-service.git
cd sample-soap-service
```

Build the project using Maven

```bash
mvn clean install
```

Run the project using Maven

```bash 
mvn spring-boot:run
```

## Apache Tomcat
The project can produce a WAR file to be installed on Apache Tomcat using the following command:

```bash 
mvn clean package
```

This will create a sample-soap-service.war file in the ~/target directory that can be deployed to TomCat.

Note: As this is a Spring Boot 3.x project it will require Apache Tomcat 10.x or higher.

## Endpoints

### ListMovies

This endpoint allows you to retrieve a list of all movies currently stored in the database.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://jonclarke.dev/movies/web-service">
    <soapenv:Header/>
    <soapenv:Body>
        <web:listMoviesRequest/>
    </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:listMoviesResponse xmlns:ns2="http://jonclarke.dev/movies/web-service">
            <ns2:movies>
                <ns2:id>1</ns2:id>
                <ns2:title>Movie 1</ns2:title>
                <ns2:description>Movie 1 Description</ns2:description>
                <ns2:releaseDate>2023-01-01T01:10:10</ns2:releaseDate>
                <ns2:availableOnDvd>true</ns2:availableOnDvd>
            </ns2:movies>
            <ns2:movies>
                <ns2:id>2</ns2:id>
                <ns2:title>Movie 2</ns2:title>
                <ns2:description>Movie 2 Description</ns2:description>
                <ns2:releaseDate>2023-12-31T23:59:59</ns2:releaseDate>
                <ns2:availableOnDvd>false</ns2:availableOnDvd>
            </ns2:movies>
        </ns2:listMoviesResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### GetMovie

This endpoint allows you to retrieve a specific movie based on its ID.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://jonclarke.dev/movies/web-service">
    <soapenv:Header/>
    <soapenv:Body>
        <web:getMovieRequest>
            <web:id>1</web:id>
        </web:getMovieRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:getMovieResponse xmlns:ns2="http://jonclarke.dev/movies/web-service">
            <ns2:movie>
                <ns2:id>1</ns2:id>
                <ns2:title>Movie 1</ns2:title>
                <ns2:description>Movie 1 Description</ns2:description>
                <ns2:releaseDate>2023-01-01T01:10:10</ns2:releaseDate>
                <ns2:availableOnDvd>true</ns2:availableOnDvd>
            </ns2:movie>
        </ns2:getMovieResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### AddMovie

This endpoint allows you to add a new movie to the database.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://jonclarke.dev/movies/web-service">
    <soapenv:Header/>
    <soapenv:Body>
        <web:addMovieRequest>
            <web:movie>
                <web:title>Movie 3</web:title>
                <web:description>Movie 3 Description</web:description>
                <web:releaseDate>2023-01-01T01:12:12</web:releaseDate>
                <web:availableOnDvd>true</web:availableOnDvd>
            </web:movie>
        </web:addMovieRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:addMovieResponse xmlns:ns2="http://jonclarke.dev/movies/web-service">
            <ns2:movie>
                <ns2:id>3</ns2:id>
                <ns2:title>Movie 3</ns2:title>
                <ns2:description>Movie 3 Description</ns2:description>
                <ns2:releaseDate>2023-01-01T01:12:12</ns2:releaseDate>
                <ns2:availableOnDvd>true</ns2:availableOnDvd>
            </ns2:movie>
            <ns2:message>Movie added successfully</ns2:message>
        </ns2:addMovieResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### UpdateMovie

This endpoint allows you to update an existing movie in the database.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://jonclarke.dev/movies/web-service">
    <soapenv:Header/>
    <soapenv:Body>
        <web:updateMovieRequest>
            <web:movie>
                <web:id>3</web:id>
                <web:title>Movie 3 - Update</web:title>
                <web:description>Movie 3 Description - Update</web:description>
                <web:releaseDate>2023-01-01T01:12:12</web:releaseDate>
                <web:availableOnDvd>true</web:availableOnDvd>
            </web:movie>
        </web:updateMovieRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:updateMovieResponse xmlns:ns2="http://jonclarke.dev/movies/web-service">
            <ns2:movie>
                <ns2:id>3</ns2:id>
                <ns2:title>Movie 3 - Update</ns2:title>
                <ns2:description>Movie 3 Description - Update</ns2:description>
                <ns2:releaseDate>2023-01-01T01:12:12</ns2:releaseDate>
                <ns2:availableOnDvd>true</ns2:availableOnDvd>
            </ns2:movie>
            <ns2:message>Movie updated successfully</ns2:message>
        </ns2:updateMovieResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### DeleteMovie

This endpoint allows you to delete a movie from the database based on its ID.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://jonclarke.dev/movies/web-service">
    <soapenv:Header/>
    <soapenv:Body>
        <web:deleteMovieRequest>
            <web:id>3</web:id>
        </web:deleteMovieRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:deleteMovieResponse xmlns:ns2="http://jonclarke.dev/movies/web-service">
            <ns2:movie>
                <ns2:id>3</ns2:id>
                <ns2:title>Movie 3 - Update</ns2:title>
                <ns2:description>Movie 3 Description - Update</ns2:description>
                <ns2:releaseDate>2023-01-01T01:12:12</ns2:releaseDate>
                <ns2:availableOnDvd>true</ns2:availableOnDvd>
            </ns2:movie>
            <ns2:message>Movie deleted successfully</ns2:message>
        </ns2:deleteMovieResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

## How to Use

To use the Movie SOAP API, you can use any SOAP client tool or library that supports sending SOAP requests. The API expects XML-based SOAP requests and responds with XML-based SOAP responses, as demonstrated in the examples above.

Here's a summary of the steps to interact with the API:

1. Send a SOAP request to one of the available endpoints (e.g., ListMovies, GetMovie, AddMovie, UpdateMovie, DeleteMovie) using the appropriate XML structure.
2. Receive the SOAP response, which will contain the requested movie data or a success/failure message.

Make sure to configure your SOAP client to send requests to the appropriate endpoint URL of the Movie SOAP API (by default this will be http://localhost:8080/ws).

The WSDL document is available at: http://localhost:8080/ws/movies.wsdl

## Warning!!!

This project was not intended for production use and is not secure. It is intended as a lightweight application for testing and experimentation purposes only.

## ToDo

- Add authentication - Add different authentication methods (Basic, OAuth, etc.) 

## References

This project is based on the tutorial: [Producing a SOAP web service](https://spring.io/guides/gs/producing-web-service/)