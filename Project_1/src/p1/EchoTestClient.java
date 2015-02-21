package p1;

import java.net.*;
import java.io.*;

import p1.Request;
import p1.Response;
import p1.Account;

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
    ObjectOutputStream ous = new ObjectOutputStream( out );
    InputStream in = socket.getInputStream ();
    ObjectInputStream ins = new ObjectInputStream (in);
    
    ous.writeObject( new NewAccountRequest("John", "Doe", "333 Maple Road"));
    // ous.writeObject( new DepositRequest(1, 200));
    // ous.writeObject( new NewAccountRequest("John2", "Doe2", "333 Maple Road2"));
    // ous.writeObject( new WithdrawRequest(1, 100));
    // ous.writeObject( new GetBalanceRequest(1));
    // ous.writeObject( new TransferRequest(1, 2, 100));




    try{
        Thread.sleep(1000);
    } catch (InterruptedException e){
        e.printStackTrace();
    }
    try {
        Response newRes = (Response) ins.readObject();

        System.out.println(newRes.getType());
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }






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
