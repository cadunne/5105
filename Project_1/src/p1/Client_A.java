package p1;

import java.net.*;
import java.io.*;

import p1.Request;
import p1.Response;
import p1.Account;

public class Client_A   {

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
    ous.writeObject( new NewAccountRequest("Fred", "Doe", "111 Chestnut Street"));

    int aid_1 = 0, aid_2 = 0, bal1 = 0, bal2 = 0;
    String status1, status2;
    Response newRes1, newRes2;

    try {
        newRes1= (Response) ins.readObject();
        newRes2 = (Response) ins.readObject();
        aid_1 = newRes1.getAccountID();
        aid_2 = newRes2.getAccountID();
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }

    //deposit 100s
    ous.writeObject( new DepositRequest(aid_1, 100));
    ous.writeObject( new DepositRequest(aid_2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        newRes2 = (Response) ins.readObject();
        status1 = newRes1.getStatus();
        status2 = newRes2.getStatus();
        System.out.println(status1+status2);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }

    // ous.writeObject( new NewAccountRequest("John2", "Doe2", "333 Maple Road2"));
    // ous.writeObject( new WithdrawRequest(1, 100));
    ous.writeObject( new GetBalanceRequest(aid_1));
    ous.writeObject( new GetBalanceRequest(aid_2));
    // ous.writeObject( new TransferRequest(1, 2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        newRes2 = (Response) ins.readObject();
        bal1 = newRes1.getBalance();
        bal2 = newRes2.getBalance();
        System.out.println(bal1+ " " + bal2);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }



    //transfer, read
    ous.writeObject( new TransferRequest (aid_1, aid_2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        status1 = newRes1.getStatus();
        System.out.println(status1);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }


    // try{
    //     Thread.sleep(1000);
    // } catch (InterruptedException e){
    //     e.printStackTrace();
    // }
    

    ous.writeObject( new GetBalanceRequest(aid_1));
    ous.writeObject( new GetBalanceRequest(aid_2));
    // ous.writeObject( new TransferRequest(1, 2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        newRes2 = (Response) ins.readObject();
        bal1 = newRes1.getBalance();
        bal2 = newRes2.getBalance();
        System.out.println(bal1+ " " + bal2);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }

/*
7. Repeat step 4 above (transfer operation); it should return fail status
8. Repeat step 3 above, and print the returned status and values
*/
    

    ous.writeObject( new WithdrawRequest(aid_1, 100));
    ous.writeObject( new WithdrawRequest(aid_2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        newRes2 = (Response) ins.readObject();
        status1 = newRes1.getStatus();
        status2 = newRes2.getStatus();
        System.out.println(status1+status2);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }


    ous.writeObject( new TransferRequest (aid_1, aid_2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        status1 = newRes1.getStatus();
        System.out.println(status1);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }


    ous.writeObject( new GetBalanceRequest(aid_1));
    ous.writeObject( new GetBalanceRequest(aid_2));
    // ous.writeObject( new TransferRequest(1, 2, 100));
    try {
        newRes1 = (Response) ins.readObject();
        newRes2 = (Response) ins.readObject();
        bal1 = newRes1.getBalance();
        bal2 = newRes2.getBalance();
        System.out.println(bal1+ " " + bal2);
    }
    catch ( ClassNotFoundException e) {
         e.printStackTrace();
    }

    // socket.close();

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
