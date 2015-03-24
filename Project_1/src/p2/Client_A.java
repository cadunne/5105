package p2;

import java.rmi.Naming;

import p2.RMI_Server;

public class Client_A {
  public static void main (String args[]) throws Exception {
    if (args.length != 1)
      throw new RuntimeException ("Syntax: DateClient <hostname>");
    RMI_Interface rmiS = (RMI_Interface) Naming.lookup ("//" + args[0] + "/RMI_Server");

    //init vars
    int aid_1, aid_2, bal1, bal2;
    String status1, status2;

    //make 2 accounts
    aid_1 = rmiS.makeNewAccount("Jason", "Lolly", "112 Test Addr");
    aid_2 = rmiS.makeNewAccount("Chris", "Spurbs", "321 Real Addr");


    //Deposit 100 in each
    status1 = rmiS.deposit(aid_1, 100);
    status2 = rmiS.deposit(aid_2, 100);
    System.out.println("Post-deposit statuses: "+status1+ " " +status2);

    //Get account balances
    bal1 = rmiS.getBalance(aid_1);
    bal2 = rmiS.getBalance(aid_2);
    System.out.println("Balances: "+bal1+ " " + bal2);


    //Transfer 100 from 1 to 2
    status1 = rmiS.transfer(aid_1, aid_2, 100);
    System.out.println("Status of first transfer attempt: "+status1);

    //Get balances again
    bal1 = rmiS.getBalance(aid_1);
    bal2 = rmiS.getBalance(aid_2);
    System.out.println("Balances again: "+bal1+ " " + bal2);

    //Withdraw 100 in each
    status1 = rmiS.withdraw(aid_1, 100);
    status2 = rmiS.withdraw(aid_2, 100);
    System.out.println("Post-withdraw statuses: "+status1+ " " +status2);


    //Try transferring last time
    status1 = rmiS.transfer(aid_1, aid_2, 100);
    System.out.println("Status of second transfer attempt: "+status1);

    //Get balances again
    bal1 = rmiS.getBalance(aid_1);
    bal2 = rmiS.getBalance(aid_2);
    System.out.println("Balances last time: "+bal1+ " " + bal2);

  }
}

