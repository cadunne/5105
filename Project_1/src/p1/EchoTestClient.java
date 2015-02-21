package p1;

import java.net.*;
import java.io.*;

public class EchoTestClient   {

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

    Socket socket = new Socket ( );
    InetSocketAddress addr = new InetSocketAddress(host, port);//new code
    socket.connect( addr, port ); //addr used to be host
    System.out.println ("Connected.");




    OutputStream out = socket.getOutputStream ();
    ObjectOutputStream os = new ObjectOutputStream( out );

    os.writeObject( new NewAccountRequest("aaa", "lastname", "addr```"));
    os.writeObject( new DepositRequest(1, 200));
    os.writeObject( new WithdrawRequest(1, 100));
    os.writeObject( new GetBalanceRequest(1));
    os.writeObject( new TransferRequest(1, 2, 100));


    socket.close();

    /*

    OutputStream rawOut = socket.getOutputStream ();
    InputStream rawIn = socket.getInputStream ();
    BufferedReader  buffreader = new BufferedReader( new InputStreamReader(rawIn) );
    PrintWriter printer = new PrintWriter(new OutputStreamWriter(rawOut));

    BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

    String line1, line2 ;
//    while ( ( line = keyboard.readLine() ) != null ) {


    while ( (line1 = keyboard.readLine()) != null) {
            printer.println(line1);
            printer.flush();
            System.out.println("echo: " + buffreader.readLine());
    }
    */


  }
}
