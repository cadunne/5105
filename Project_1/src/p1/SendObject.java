
import java.net.*;
import java.io.*;
import java.util.*;

public class SendObject {

  protected String host, file;
  protected int port;
  protected DataInputStream in;
  protected DataOutputStream out;


  public static void main (String args[]) throws IOException {
    InetAddress  server  = null;
    Socket      sock = null;
    String host = args[0];
    int  port = Integer.parseInt( args[1] );

    if ( args.length != 2 ) {
       throw new RuntimeException( "hostname and port number as arguments" );
    }

    System.out.println ("Connecting to " + host + ":" + port + "..");

    Socket socket = new Socket (host, port);
    System.out.println ("Connected.");

    OutputStream out = socket.getOutputStream ();
    ObjectOutputStream os = new ObjectOutputStream( out );

    os.writeObject( new Date() );
    os.writeObject( "Anand R. Tripathi");
    socket.close();

  }
}
