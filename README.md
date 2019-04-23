# Android Simple Chat 
This is a simple android chat sample using socket.

It also uses following features.
 - REST API for getting & updating MySQL data.
 - RxJava2
 - MVVM pattern

Simple Interface
-------------------
No need to sign up for ID. 

You can choose one among three IDs to sign in, which are RED, YELLOW and BLUE.  

So three devices can be used to test the app.

You can chat with single client(p2p), and also with multiple clients(groupMsg). 

Chat Messages are saved by DBManager using SQLIite. 


Java Socket Server
-----------
You can find java socket server files from <a href= "https://github.com/Techkwon/simple-chat-java-server">here.</a>

Java server handles clients message.

When client_A wants to send message to client_B, 
server receives the object of ChatMessage class checking who is the receiver and then pass the object to client B. 

<br/>
### Sign In
<img src="img/sign in.jpg" alt="sign_in"><br/>


### Choose Member to Talk
<img src="img/show members.jpg" alt="members"><br/>


### Chat Room
<img src="img/chat view.jpg" alt="chat"><br/>


### Update Name at DB
<img src="img/update database.jpg" alt="update"><br/>
