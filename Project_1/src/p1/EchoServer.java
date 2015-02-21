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
  protected FileWriter fileLog;
  protected Hashtable<Integer, Account> hTable;

  EchoServer (Socket s, Hashtable<Integer, Account> ht) {
    System.out.println ("New client.");
    this.s = s;
    this.hTable = ht;
    try{
      this.fileLog = new FileWriter("MyFile.txt", true);
    }
    catch(IOException ioe){
        System.err.println("IOException: " + ioe.getMessage());
    }
  } 


  private synchronized int makeNewAccount(String firstName, String lastName, String address){
    int accountID = this.hTable.size() + 1;

    Account acc = new Account(accountID, 0, firstName, lastName, address); //fix accountID
    this.hTable.put(accountID, acc);

    System.out.println("hash table: "+this.hTable);

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
    try
    {
        fileLog.write("add a line\n");//appends the string to the file
        fileLog.close();
    }
    catch(IOException ioe)
    {
        System.err.println("IOException: " + ioe.getMessage());
    }

    

 

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
        System.out.println(newReq.getType());
        System.out.println(newReq);

        if(newReq.getType().equals("NewAccountRequest")){
          newReq = (NewAccountRequest)newReq;
          System.out.println("NewAccount request made with name '"+newReq.getFirstName()+" "+newReq.getLastName()+"' and address '"
            + newReq.getAddress()+"'.");

          int newAccountID =  makeNewAccount(newReq.getFirstName(), newReq.getLastName(), newReq.getAddress());

          //send back newAccountID
          oout.writeObject( new NewAccountResponse(newAccountID));
          oout.flush();




        }else if(newReq.getType().equals("DepositRequest")){
          newReq = (DepositRequest)newReq;
          System.out.println("Deposit request made for account '" + newReq.getAccountID() + "' of amount '" + newReq.getAmount()+"'.");

          String status = deposit(newReq.getAccountID(), newReq.getAmount());

          oout.writeObject( new DepositResponse(status));
          oout.flush();

        }else if(newReq.getType().equals("WithdrawRequest")){
          newReq = (WithdrawRequest)newReq;
          System.out.println("Deposit request made for account '" + newReq.getAccountID() + "' of amount '" + newReq.getAmount()+"'.");

          String status = withdraw(newReq.getAccountID(), newReq.getAmount());

          oout.writeObject( new WithdrawResponse(status));
          oout.flush();

        }else if(newReq.getType().equals("GetBalanceRequest")){
          newReq = (GetBalanceRequest)newReq;
          System.out.println("Balance request made for account '" + newReq.getAccountID() + "'.");          

          int balance = getBalance(newReq.getAccountID());

          oout.writeObject( new GetBalanceResponse(balance));
          oout.flush();

          //send back balance

        }else if(newReq.getType().equals("TransferRequest")){
          newReq = (TransferRequest)newReq;
          System.out.println("Transfer request made from account '" + newReq.getAccountID() + "' to account '" + newReq.getTargetID() +
            "' of amount '" + newReq.getAmount()+"'.");

          String status = transfer(newReq.getAccountID(), newReq.getTargetID(), newReq.getAmount());

          oout.writeObject( new TransferResponse(status));
          oout.flush();

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
        // System.out.println("Hash table: " + hTable);
      } catch (IOException ex) {
        ex.printStackTrace ();
      }
    }
  }

  public static void main (String args[]) throws IOException {

    //delete file log to start new one.

    // Request req = new GetBalanceRequest(55);
    // Response res = new GetBalanceResponse(10);
    // System.out.println(req.getType());
    // System.out.println(res.getType());


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
