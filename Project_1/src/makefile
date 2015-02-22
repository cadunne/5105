all: 
	javac p1/Account.java p1/Response.java p1/Request.java p1/EchoServer.java p1/Client_B.java p1/Client_A.java

clean:
	rm p1/*.class serverLogFile.txt clientLogFile.txt

server:
	java p1/EchoServer 5113

client:
	java p1/Client_B localhost 5113

clientt:
	java p2/Client_A localhost:1111
	

p2:
	javac p2/RMI_Interface.java p2/RMI_Server.java p2/Client_A.java
