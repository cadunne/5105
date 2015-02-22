package p2;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.registry.*;
import java.io.*;
import java.util.Hashtable;


import p2.RMI_Interface;
import p2.Account;

/*  First run the rmiregistry by typing

                             rmiregistry &

    This will start the rmiregistry on port 1099.

    OR
                             rmiregistry somePortNumber&

    This will start rmiregistry on the port specified by the argument.

    Run the server as follows:

        If rmiregistry is on default port 1099, you type:

                             java  RMI_Server                  


        If rmiregistry is on some other port, you type:

                             java  RMI_Server rmiPortNumber    
*/

public class RMI_Server extends UnicastRemoteObject implements RMI_Interface {


  protected Hashtable<Integer, Account> hTable;

  public RMI_Server (Hashtable<Integer, Account> hTable) throws RemoteException {
     super( 5051 );
     this.hTable = hTable;
  }


  private synchronized void writeToLog(String contents){
    try{
        FileWriter fw = new FileWriter("rmiServerLogFile.txt", true);
        fw.write(contents + "\n");
        fw.flush();
      }
      catch(IOException e){
        e.printStackTrace();
      }
  }

  public int makeNewAccount(String firstName, String lastName, String address){
    synchronized(this){
      int accountID = this.hTable.size() + 1;
      Account acc = new Account(accountID, 0, firstName, lastName, address);
      this.hTable.put(accountID, acc);

      writeToLog("Request: NewAccount. First/Last/Address: "+firstName+"/"+lastName+"/"+address +". Response: "+accountID);

      System.out.println(this.hTable);

      return accountID;
    }
  }

  public String deposit(int accountID, int amount){
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


    writeToLog("Request: Deposit. AccountID/amount: "+accountID+"/"+amount+". Response: "+status);



    return status;
  }

  public String withdraw(int accountID, int amount){
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
    
    writeToLog("Request: Withdraw. AccountID/amount: "+accountID+"/"+amount+". Response: "+status);


    return status;
  }

  public int getBalance(int accountID){
    int amount = 0;

    Account acc = this.hTable.get(accountID);
    amount = acc.getBalance();
    
    writeToLog("Request: GetBalance. AccountID: "+accountID+". Response: "+amount);

    return amount;
  }
  public String transfer(int accountID, int targetID, int amount){
    synchronized(this){
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
          System.out.println("bals: "+aBal+" "+tBal);
          acc.setBalance(aBal);
          tar.setBalance(tBal);

          status = "OK";
        }
      }

      writeToLog("Request: Transfer. AccountID/targetID/amount: "+accountID+"/"+targetID+"/"+amount+". Response: "+status);
      return status;

    }
  }

  public static void main (String args[]) throws Exception {

    System.setSecurityManager (new RMISecurityManager ());
    Hashtable<Integer, Account> newHt = new Hashtable<Integer, Account>();


    RMI_Server rmiS = new RMI_Server (newHt);

    if ( args.length == 0 ) {
      // If no port number is given for the rmiregistry, assume it is on the default port 1099
       Naming.bind ("RMI_Server", rmiS);
    }
    else {
       // rmiregistry is on port specified in args[0]. Bind to that registry.
       Registry localRegistry = LocateRegistry.getRegistry( Integer.parseInt( args[0] ));
       localRegistry.bind ("RMI_Server", rmiS);
    }
    System.out.println("RMI_Server Running!");
  }
}
