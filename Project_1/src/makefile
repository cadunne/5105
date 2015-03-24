clean:
	rm p1/*.class p2/*.class *.txt

server:
	java p1/EchoServer 5113

client:
	java p1/Client_B localhost 5113

clientt:
	java p2/Client_A localhost:1111
	
p1:
	javac p1/Account.java p1/Response.java p1/Request.java p1/EchoServer.java p1/Client_B.java p1/Client_A.java

p2:
	javac p2/Account.java p2/RMI_Interface.java p2/RMI_Server.java p2/Client_A.java p2/Client_B.java
