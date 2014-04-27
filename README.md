Play + Android + Browser = PROFIT
=================================

This project shows how simple integrating Android and WebBrowser via WebSocket in PlayFramework is.
This repository contains two main directories: PlayWeb - contains Play project, which serves as WebSocket server and also as web server with our browser page, which connects to Play's Netty server. Android - project with client application that connects to Play with WebSocket protocol.

But Why?
--------

1. **Fun**
2. Single protocol for both ways
3. **It's really damn fast**

WebSocket are really useful when You want to present real-time data without many ajax calls with really small interval.

How simple it is?
-----------------

It depends on case, let's start with easier one.

### Play Framework 2.XX

Play commes with build in WebSocket support. To make them work we need to actually do two things.

#### Add route to router
Just add single line to routes file

```
GET     /socket                           controllers.Application.socket()
```
where **/socket** is route that Your WebSocket client have to access and **controllers.Application.socket()** is simply Your Controllers method. Just like in normal Play Application.

#### Add controller method
Now we have to handle incoming WebSocket connection

```java
public static WebSocket<String> socket() {
  return new WebSocket<String>() {
      
    // Called when the Websocket Handshake is done.
    public void onReady(WebSocket.In<String> in, WebSocket.Out<String> out) {
      
      // For each event received on the socket,
      in.onMessage(new Callback<String>() {
         public void invoke(String event) {
             
           // Log events to the console
           println(event);  
             
         } 
      });
      
      // When the socket is closed.
      in.onClose(new Callback0() {
         public void invoke() {
             
           println("Disconnected")
             
         }
      });
      
      // Send a single 'Hello!' message
      out.write("Hello!");
      
    }
    
  }
}
```

We simply return instance of WebSocket class instead of default Result. Class contains single method **onReady** which is called after connection got estabilished. This method take two parameters -  instance of WebSocket<>.In, and WebSocket<>.Out. These two classes handler in/out operations - just like their names say. But this method is quite dirty, You should extend this class and customize it for Your own needs look at my example - MyWebSocket class in *WebPlay* project.