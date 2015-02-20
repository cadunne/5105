
import java.net.*;
import java.io.*;

public class EchoClient   {

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

    OutputStream rawOut = socket.getOutputStream ();
    InputStream rawIn = socket.getInputStream ();
    BufferedReader  buffreader = new BufferedReader( new InputStreamReader(rawIn) );
    PrintWriter printer = new PrintWriter(new OutputStreamWriter(rawOut));

    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    String line ;
    while ( ( line = keyboard.readLine() ) != null ) {
            printer.println( line );
            printer.flush();
    }

    while ( ( line = buffreader.readLine() ) != null ) {
            System.out.println( line );
    }
  }
}
