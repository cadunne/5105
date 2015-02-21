package p1;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Hashtable;

import p1.Request;
import p1.Response;
import p1.Account;


public class EchoServer extends Thread {
  protected Socket s;
  protected Hashtable<Integer, Account> hTable;

  EchoServer (Socket s, Hashtable<Integer, Account> ht) {
    System.out.println ("New client.");
    this.s = s;
    this.hTable = ht;
  } 

  private synchronized void writeToLog(String contents){
    try{
        FileWriter fw = new FileWriter("serverLogFile.txt", true);
        fw.write(contents + "\n");
        fw.flush();
      }
      catch(IOException e){
        e.printStackTrace();
      }
  }


  private synchronized int makeNewAccount(String firstName, String lastName, String address){
    int accountID = this.hTable.size() + 1;

    Account acc = new Account(accountID, 0, firstName, lastName, address); //fix accountID
    this.hTable.put(accountID, acc);

    return accountID;
  }

  private String deposit(int accountID, int amount){
    String status = ""; //OK or FAIL
    if(amount < 0){
      status = "FAIL";
    }
    else{
      Account acc = this.hTable.get(accountID);
      int bal = acc.getBalance();
      bal += amount;
      acc.setBalance(bal);
      status = "OK";
    }

    return status;
  }

  private String withdraw(int accountID, int amount){
    String status = ""; //OK or FAIL
    if(amount < 0){
      status = "FAIL";
    }
    else{
      Account acc = this.hTable.get(accountID);
      int bal = acc.getBalance();
      if(bal - amount <= 0){
        status = "FAIL";        
      }
      else{
        bal -= amount;
        acc.setBalance(bal);
        status = "OK";
      }
    }

    return status;
  }

  private int getBalance(int accountID){
    int balance = 0;

    Account acc = this.hTable.get(accountID);
    balance = acc.getBalance();

    return balance;
  }

  private synchronized String transfer(int accountID, int targetID, int amount){
    String status = ""; //OK or FAIL

    if(amount < 0){
      status = "FAIL";
    }
    else{
      Account acc = this.hTable.get(accountID);
      Account tar = this.hTable.get(targetID);
      int aBal = acc.getBalance();
      int tBal = tar.getBalance();

      if(aBal - amount < 0){
        status = "FAIL";
      }
      else{
        aBal -= amount;
        tBal += amount;
        acc.setBalance(aBal);

        tar.setBalance(tBal);
        status = "OK";
      }
    }
    
    return status;
  }




  public void run () {
    try {


      InputStream istream = s.getInputStream ();
      ObjectInputStream oin = new ObjectInputStream (istream);
      OutputStream ostream = s.getOutputStream ();
      ObjectOutputStream oout = new ObjectOutputStream(ostream);

      // PrintWriter outp = new PrintWriter (ostream, true);
      // outp.println ("Welcome to the multithreaded echo server."); //not being written right away

      Request newReq = null;

      // Date date = null;
      // String name = null;
    
      while((newReq = (Request) oin.readObject()) != null){
        if(newReq.getType().equals("NewAccountRequest")){
          newReq = (NewAccountRequest)newReq;
          int newAccountID =  makeNewAccount(newReq.getFirstName(), newReq.getLastName(), newReq.getAddress());

          //send back newAccountID
          oout.writeObject( new NewAccountResponse(newAccountID));
          oout.flush();

          writeToLog("Request: NewAccount. First/Last/Address: "+newReq.getFirstName()+"/"+newReq.getLastName()+"/"+newReq.getAddress() +
          ". Response: "+newAccountID);



        }else if(newReq.getType().equals("DepositRequest")){
          newReq = (DepositRequest)newReq;
          String status = deposit(newReq.getAccountID(), newReq.getAmount());

          oout.writeObject( new DepositResponse(status));
          oout.flush();

          writeToLog("Request: Deposit. AccountID/amount: "+newReq.getAccountID()+"/"+newReq.getAmount()+". Response: "+status);

        }else if(newReq.getType().equals("WithdrawRequest")){
          newReq = (WithdrawRequest)newReq;
          String status = withdraw(newReq.getAccountID(), newReq.getAmount());

          oout.writeObject( new WithdrawResponse(status));
          oout.flush();

          writeToLog("Request: Withdraw. AccountID/amount: "+newReq.getAccountID()+"/"+newReq.getAmount()+". Response: "+status);

        }else if(newReq.getType().equals("GetBalanceRequest")){
          newReq = (GetBalanceRequest)newReq;
          int balance = getBalance(newReq.getAccountID());

          oout.writeObject( new GetBalanceResponse(balance));
          oout.flush();

          writeToLog("Request: GetBalance. AccountID: "+newReq.getAccountID()+". Response: "+balance);

          //send back balance

        }else if(newReq.getType().equals("TransferRequest")){
          newReq = (TransferRequest)newReq;
          String status = transfer(newReq.getAccountID(), newReq.getTargetID(), newReq.getAmount());

          oout.writeObject( new TransferResponse(status));
          oout.flush();

          writeToLog("Request: Transfer. AccountID/targetID/amount: "+newReq.getAccountID()+"/"+newReq.getTargetID()+"/"
            +newReq.getAmount()+". Response: "+status);

        }
        else{
          System.out.println("Invalid request");//invalid request
        }
      }
    }
    catch ( ClassNotFoundException e) {
       e.printStackTrace();
    }
    catch (EOFException e) {
      //end of file, simply continue on
    }
      

    catch (IOException ex) {
      ex.printStackTrace ();
    } finally {
      try {
        s.close ();
        System.out.println ("Client exit.");
      } catch (IOException ex) {
        ex.printStackTrace ();
      }
    }
  }

  public static void main (String args[]) throws IOException {
    //delete log file each time the server is run
    File f = new File("serverLogFile.txt");
    f.delete();


    Hashtable<Integer, Account> newHt = new Hashtable<Integer, Account>();


    if (args.length != 1)
         throw new RuntimeException ("Syntax: EchoServer port-number");

    System.out.println ("Starting on port " + args[0]);
    ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

    while (true) {
      System.out.println ("Waiting for a client request");
      Socket client = server.accept ();
      System.out.println ("Received request from " + client.getInetAddress ());
      
      EchoServer c = new EchoServer (client, newHt);
      c.start ();
    }
  }
}
