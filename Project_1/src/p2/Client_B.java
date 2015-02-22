package p2;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Random;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;

import p2.RMI_Server;

public class Client_B extends Thread  {


  protected String host, file;
  protected int port;
  protected int iterationCount;
  protected int[] aids;
  protected DataInputStream in;
  protected DataOutputStream out;

  protected Object semaphore = 1;

  Client_B (String host, int iterationCount, int[] accounts) {
    this.host = host;
    this.iterationCount = iterationCount;
    this.aids = accounts;
    // System.out.println ("New client.");
  } 

  private void writeToLog(String contents){
    synchronized(this.semaphore){
        try{
            FileWriter fw = new FileWriter("rmiClientLogFile.txt", true);
            fw.write(contents + "\n");
            fw.close();
          }
          catch(IOException e){
            e.printStackTrace();
          }
    }
  }

  public void run(){

    // try{
    //     Thread.sleep(1000);
    // }catch(InterruptedException e){
    //     //do nothing
    // }

    RMI_Interface rmiS = null;
    try{
        rmiS = (RMI_Interface) Naming.lookup ("//" + this.host + "/RMI_Server");
    }catch(java.rmi.RemoteException e){
        //
    }
    catch(MalformedURLException e){
        //
    }catch(java.rmi.NotBoundException e){
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
            String status = rmiS.transfer(aid_1, aid_2, 10);
            if(status.equals("FAIL")){
                System.out.println("failed");
                writeToLog("FAIL return status when transferring between accounts "+aid_1+" and "+aid_2+".");
            }
        }catch(java.rmi.RemoteException e){
            //nothing
        }
    }

    // }
    // //terminate

    // try{
    //     Thread.sleep(1000);
    // }catch(InterruptedException e){

    // }
  }


  public static void main (String args[]) throws IOException {
    System.setSecurityManager (new RMISecurityManager ()); //set here?
    RMI_Interface rmiS = null;
    try{
        rmiS = (RMI_Interface) Naming.lookup ("//" + args[0] + "/RMI_Server");
    }catch(java.rmi.NotBoundException e){
        //do nothing
    }

    //make sure log is made
    FileWriter fw = new FileWriter("rmiClientLogFile.txt", true);
    fw.write("");
    fw.close();

    int  threadCount = Integer.parseInt( args[1] );
    int  iterationCount = Integer.parseInt( args[2] );

    if ( args.length != 3 ) {
       throw new RuntimeException( "hostname, threadCount, and iterationCount as arguments" );
    }

    //local vars
    int[] aids = new int[100];
    int bal1;
    String status1;

    //gen 100 accounts, store them in aids[]
    for(int i=0; i<100; i++){
        aids[i] = rmiS.makeNewAccount("Jason", "Lolly", "112 Test Addr");
    }

    //deposit 100 in all created accounts
    for(int i=0; i<100; i++){
        int accID = aids[i];
        status1 = rmiS.deposit(accID, 100); //should all succeed
    }

    //print total balance
    int totalBal = 0;
    for (int i=0; i<100; i++){
        int accID = aids[i];
        bal1 = rmiS.getBalance(accID);
        totalBal += bal1;
    }
    System.out.println("Total before threads: "+totalBal);



    //create threads and join all of them to this main one.
    Client_B[] threads = new Client_B[threadCount];

    for (int i=0; i<threadCount; i++){
        Client_B c = new Client_B (args[0], iterationCount, aids);
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
        bal1 = rmiS.getBalance(accID);
        totalBal += bal1;
    }
    System.out.println("Total after threads: "+totalBal);


  }
}
