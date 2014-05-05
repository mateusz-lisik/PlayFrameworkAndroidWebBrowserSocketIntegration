Play + Android + Browser = PROFIT
=================================

This project shows how simple integrating Android and WebBrowser via WebSocket in PlayFramework is.
This repository contains two main directories: PlayWeb - contains Play project, which serves as WebSocket server and also as web server with our browser page, which connects to Play's Netty server. Android - project with client application that connects to Play with WebSocket protocol.

Kickstarting this example
=================
If You dont want to be guided step-by-step and just run example from this repository just go to MainActivity.java in Android example and change IP address to corresponding with Your PC (look for WebSocket's constructor usage). Now You can connect Smartphone to Play instance and see changes in web browser.

But Why?
--------

1. **Fun**
2. Single protocol for both ways
3. **It's really damn fast**

WebSocket are really useful when You want to present real-time data without many ajax calls with really small interval.

How simple it is?
-----------------

Fairly easy, let's start with easier one.

### Play Framework 2.XX

Play commes with build in WebSocket support. To make them work we need to actually do two things.

#### Add route to router
Just add single line to routes file

```
GET     /socket                           controllers.Application.socket()
```
where **/socket** is route that Your WebSocket will access, and **controllers.Application.socket()** is simply Your Controllers method. Just like in normal Play Application.

*According to WebSockets specification, server handshake is handled by by **GET** request, which responses with 101 Upgrade protocol. That makes GET an obligatory request type to estabilish WebSocket connection.*
http://en.wikipedia.org/wiki/WebSocket#WebSocket_protocol_handshake


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

We simply return instance of WebSocket class instead of default Result. Class contains single method **onReady** which is called after connection got estabilished. This method takes two parameters -  instance of WebSocket<>.In, and WebSocket<>.Out. These two classes handle in/out operations - just like their names say. But this method is quite dirty, You should extend this class and customize it for Your own needs. Look at my example - [**MyWebSocket**](../blob/master/PlayWeb/app/common/MyWebSocket.java) class in *WebPlay* project.

### Android App
Android by default does not have WebSockets support, but fortunetly GitHub user **TooTallNate** made a remarkable library for WebSockets - [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket). It may act as client, and as server aswell. In this example we will focus on its client capabilities. 

#### Preparing and sending message
First, You have to start new Android project, and using Maven add Java-WebSocket to Your project, after that, You are ready to go. Proceed to Your main activity. 

Find **onCreate** method, and add there following lines:
```java
try {
    ws = new WebSocketClient(new URI("ws://YOUR_DESKTOP_IP_ADDRESS:9000/socket")) {
        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            
        }

        @Override
        public void onMessage(String s) {

        }

        @Override
        public void onClose(int i, String s, boolean b) {

        }

        @Override
        public void onError(Exception e) {

        }
} catch (URISyntaxException e) {
    e.printStackTrace();
}


```

> Remark: If You use Android Emulator (AVD) You have to add these two lines before You open any connection:
>```java
java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
>```
> AVD has some problems with IPv6, and You might end with hard to debug problems, when You dont need them.

Now You have to connect to server by using ws.connect or ws.connectBlocking method. Second one blocks thread while library is trying to estabilish connection. We can now send something to server and check play console if we got some output. To do this, we have to use ws.send method.
```java
ws.connectBlocking();
ws.send("Iam an Android");
```

### Webbrowser's websocket
In last part of this example we have to go back to our Play project and work on Webbrowser part of implementation.
There is really nice tutorial on [WebSocket.org](http://www.websocket.org/echo.html). But for example sake we will only use basics.

### Preparing
We should execute code when DOM document is ready, easiest method is to use jQuery.ready method. You can use [google hosted libraries](https://developers.google.com/speed/libraries/devguide) if You don't want to download it on Your HDD. Just include following line in Your index.scala.html file
```html
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
```
Now we have to open new WebSocket connection.
```javascript
$(document).ready(function(){
        var connection = new WebSocket('ws://localhost:9000/socket');
...
```
To catch incomming communication we need to attach our own functions which will handle communication
```javascript
    connection.onmessage = function(e) {
            console.log(e);
}
```
Function takes single argument - event. To access actual message You have to access data property.
And its basicly everything, clone repo - check example and create nice piece of software!