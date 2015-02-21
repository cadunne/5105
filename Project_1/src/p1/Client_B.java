package p1;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Random;

import p1.Request;
import p1.Response;
import p1.Account;

public class Client_B extends Thread  {


  protected String host, file;
  protected int port;
  protected int iterationCount;
  protected int[] aids;
  protected DataInputStream in;
  protected DataOutputStream out;

  protected Object semaphore = 1;

  Client_B (String host, int port, int iterationCount, int[] accounts) {
    this.host = host;
    this.port = port;
    this.iterationCount = iterationCount;
    this.aids = accounts;
    // System.out.println ("New client.");
  } 

  private void writeToLog(String contents){
    synchronized(this.semaphore){
        try{
            FileWriter fw = new FileWriter("clientLogFile.txt", true);
            fw.write(contents + "\n");
            fw.close();
          }
          catch(IOException e){
            e.printStackTrace();
          }
    }
  }

  public void run(){

    // System.out.println ("Connecting to " + host + ":" + port + "..");

    Socket socket = new Socket ( );
    InetSocketAddress addr = new InetSocketAddress(this.host, this.port);//new code
    ObjectOutputStream ous = null;
    ObjectInputStream ins = null;
    try{
        socket.connect( addr, this.port ); //addr used to be host
        // System.out.println ("Connected.");
        OutputStream out = socket.getOutputStream ();
        ous = new ObjectOutputStream( out );
        InputStream in = socket.getInputStream ();
        ins = new ObjectInputStream (in);
    }catch(IOException e){
        //do nothing
    }




    for(int i=0; i<iterationCount; i++){
        //pic 2 random accounts
        Random generator = new Random(); 
        int j, k, aid_1, aid_2;
        
        //get two random, not-same ints
        j = generator.nextInt(100);
        do{
            k = generator.nextInt(100);
        }while(k==j);

        aid_1 = aids[j]; aid_2 = aids[k];

        try{
            ous.writeObject( new TransferRequest (aid_1, aid_2, 10));   
            Response newRes1 = (Response) ins.readObject();
            String status1 = newRes1.getStatus();
            if(status1.equals("FAIL")){
                writeToLog("FAIL return status when transferring between accounts "+aid_1+" and "+aid_2+".");
            }
        }
        catch ( ClassNotFoundException e) {
             e.printStackTrace();
        }
        catch ( IOException e){
            e.printStackTrace();
        }

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
    int  threadCount = Integer.parseInt( args[2] );
    int  iterationCount = Integer.parseInt( args[3] );


    //make sure log is made
    FileWriter fw = new FileWriter("clientLogFile.txt", true);
    fw.write("");
    fw.close();

    if ( args.length != 4 ) {
       throw new RuntimeException( "hostname, port, threadCount, and iterationCount as arguments" );
    }

    // System.out.println ("Connecting to " + host + ":" + port + "..");

    Socket socket = new Socket ( );
    InetSocketAddress addr = new InetSocketAddress(host, port);//new code
    socket.connect( addr, port ); //addr used to be host
    // System.out.println ("Connected.");

    //setup streams
    OutputStream out = socket.getOutputStream ();
    ObjectOutputStream ous = new ObjectOutputStream( out );
    InputStream in = socket.getInputStream ();
    ObjectInputStream ins = new ObjectInputStream (in);

    //local vars
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

    //deposit 100 in all created accounts
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

    //print total balance
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
    System.out.println("Total before threads: "+totalBal);



    //create threads and join all of them to this main one.
    Client_B[] threads = new Client_B[threadCount];

    for (int i=0; i<threadCount; i++){
        Client_B c = new Client_B (host, port, iterationCount, aids);
        c.start ();
        threads[i] = c;
    }

    System.out.println("Threads All Started");

    for(Thread thread : threads){
        try {
            thread.join();
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    System.out.println("Threads finished");

    //check if balance is correct after all threads have finished
    totalBal = 0;
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
    System.out.println("Total after threads: "+totalBal);


  }
}
