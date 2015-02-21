package p1;

import java.net.*;
import java.io.*;
import java.lang.Thread;

import p1.Request;
import p1.Response;
import p1.Account;

public class Client_B extends Thread  {

  protected String host, file;
  protected int port;
  protected DataInputStream in;
  protected DataOutputStream out;

  Client_B (String host, int port) {
    this.host = host;
    this.port = port;
    System.out.println ("New client.");
    
    

  } 

  public void run(){

    // System.out.println ("Connecting to " + host + ":" + port + "..");

    // Socket socket = new Socket ( );
    // InetSocketAddress addr = new InetSocketAddress(host, port);//new code
    // socket.connect( addr, port ); //addr used to be host
    // System.out.println ("Connected.");

    for(int i=0; i<10; i++){
        //pick 2 random accounts
        //transfer 10 between them

    }
    //terminate

    try{
        Thread.sleep(1000);
    }catch(InterruptedException e){

    }
  }


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

    int[] aids = new int[100];
    Response newRes;

    //gen 100 accounts, store them in aids[]
    for(int i=0; i<100; i++){
        ous.writeObject( new NewAccountRequest("John", "Doe", "333 Maple Road"));
        try {
            newRes = (Response) ins.readObject();
            aids[i] = newRes.getAccountID();
        }
        catch ( ClassNotFoundException e) {
             e.printStackTrace();
        }
    }

    for(int i=0; i<100; i++){
        int accID = aids[i];
        ous.writeObject( new DepositRequest(accID, 100));
        try {
            newRes = (Response) ins.readObject();
        }
        catch ( ClassNotFoundException e) {
             e.printStackTrace();
        }
    }

    //print total bal
    int totalBal = 0;
    for (int i=0; i<100; i++){
        int accID = aids[i];
        ous.writeObject( new GetBalanceRequest(accID));
        try {
            newRes = (Response) ins.readObject();
            int bal = newRes.getBalance();
            totalBal += bal;
        }
        catch ( ClassNotFoundException e) {
             e.printStackTrace();
        }
    }
    System.out.println(totalBal);



    Client_B[] threads = new Client_B[100];


    for (int i=0; i<100; i++){
        Client_B c = new Client_B (host, port);
        c.start ();
        threads[i] = c;
        // try {
        //     c.join();
        // } catch (InterruptedException e) {
        //     //do nothing
        // }        threads[i] = c;
    }

    System.out.println("100 Threads All Started");

    for(Thread thread : threads){
        try {
            thread.join();
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    System.out.println("100 Threads finished");



  }
}
