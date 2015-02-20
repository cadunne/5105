import java.net.*;
import java.io.*;
import java.util.*;

public class GetObject {

  public static void main (String args[]) throws IOException {

    if (args.length != 1)
         throw new RuntimeException ("Syntax: EchoServer port-number");

    System.out.println ("Starting on port " + args[0]);
    ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

    while (true) {
      System.out.println ("Waiting for a client request");
      Socket client = server.accept ();
	  
	  InputStream in  = client.getInputStream();
      ObjectInputStream oin = new ObjectInputStream( in );
      Date date = null;
      String name = null;
      try {
         date = (Date) oin.readObject();
         name = (String) oin.readObject();
      }
      catch ( ClassNotFoundException e) {
         e.printStackTrace();
      }

      System.out.println( "Name: "+  name+  ", Date: "+ date );

    }
  }
}
