# Sports-Exercise-Battle

Current Flow is: 

The ServerRequest Handler is the global request handler, delegates the routing handling to the Router

The Router stores a RouteKey and a RouteHandler for each controller 


The Router delegates the HTTPRequest to the correct Endpoint through a functional interface declared in the RouteHandler
It allows controller methods to be registered as endpoint handlers

Example:
```java 
router.post("/login", authController::login)
```

The Endpoint calls it and can send back various Exceptions (BadRequest Exception, )

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

Because we are not doing Threading, Server cannot handle mutliple tasks asynchronously


TODO 
better exception handling at the controller level 