# Sports-Exercise-Battle

# How to start the server
First make sure that the Postgres SQL DB is running `docker compose up -d` and when you don't want 
the DB to persists tear it down `docker compose down -v`, this will clean up the DB. 

The main class file is located in the src root and will create the router with the corresponding endpoints via their controllers. 
It will create the ServerRequestHandler for global exception handling and the HTTPConnectionHandler to handle client requests and sends back responses back to the client.
A DBInitializer class is used to create the specific tables (tournaments, pushup records, users) to let the data persists in the SQL DB. 

Then it will start the server to listen to incoming client connection (accepting the client socket).

# Unique Feature
A unqiue feature of achievements have been added to the project. It goes through the endpoint `/achievements` and will send back 
some custom achievements based on the elo and the total pushups of the user

# Testing 
You can run the test through `mvn test` and it will test the project. The custom automated integration tests for the unique features have also been 
provided in the project root `custom_seb.curl.bat`, which tests if the achievement endpoint works correctly. 

## Request/Response Flow

```text
Client / curl / Postman
        ↓
Socket input bytes
        ↓
HttpConnectionHandler reads the raw request from the client socket
        ↓
Raw request bytes are converted into a String
        ↓
HttpRequestParser converts the raw HTTP String into an HttpRequest object
        ↓
HttpConnectionHandler passes the HttpRequest to the ServerRequestHandler
        ↓
ServerRequestHandler performs global request handling and delegates to the Router
        ↓
Router uses the RouteKey and RouteHandler to find the correct controller endpoint
        ↓
Controller handles the endpoint and returns an HttpResponse
        ↓
HttpResponse is returned through Router and ServerRequestHandler
        ↓
HttpConnectionHandler converts the HttpResponse into a raw HTTP response String/bytes
        ↓
HttpConnectionHandler writes the bytes to the client socket output stream
        ↓
Client receives the HTTP response
```

# Personal Notes for the Project 
Current Flow is:

The ServerRequest Handler is the global request handler, delegates the routing handling to the Router. 
The Router stores a RouteKey and a RouteHandler for each controller.
The Router delegates the HTTPRequest to the correct Endpoint through a functional interface declared in the RouteHandler.
It allows controller methods to be registered as endpoint handlers

Example:
```java 
router.post("/login", authController::login)
```

The Endpoint calls it and can send back various Exceptions (BadRequest Exception)
