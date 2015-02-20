all: 
	javac p1/Account.java p1/Request.java p1/EchoServer.java p1/EchoTestClient.java

clean:
	rm p1/*.class

server:
	java p1/EchoServer 5111

client:
	java p1/EchoTestClient localhost 5111
	
