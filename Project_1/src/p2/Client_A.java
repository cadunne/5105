package p2;

import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.util.Date;

import p2.RMI_Server;

/*  Usage   -  java DateClient Server-Host-DNS-name:rmiregistry-port-number-on-that-host
    E.g.       java DateClient deca.cs.umn.edu:60000
               Server is running on deca.cs.umn.edu and the rmiregistry on that host is on port 60000
*/


public class Client_A {
  public static void main (String args[]) throws Exception {
    if (args.length != 1)
      throw new RuntimeException ("Syntax: DateClient <hostname>");
    System.setSecurityManager (new RMISecurityManager ());
    RMI_Interface rmiS = (RMI_Interface) Naming.lookup ("//" + args[0] + "/RMI_Server");
    int newAcc = rmiS.makeNewAccount("FF", "LL", "----");

    System.out.println (newAcc);
  }
}


// package p1;

// import java.net.*;
// import java.io.*;

// import p1.Request;
// import p1.Response;
// import p1.Account;

// public class Client_A   {

//   protected String host, file;
//   protected int port;
//   protected DataInputStream in;
//   protected DataOutputStream out;


//   public static void main (String args[]) throws IOException {
//     InetAddress  server  = null;
//     Socket      sock = null;
//     String host = args[0];
//     int  port = Integer.parseInt( args[1] );

//     if ( args.length != 2 ) {
//        throw new RuntimeException( "hostname and port number as arguments" );
//     }

//     System.out.println ("Connecting to " + host + ":" + port + "..");

//     Socket socket = new Socket ( );
//     InetSocketAddress addr = new InetSocketAddress(host, port);//new code
//     socket.connect( addr, port ); //addr used to be host
//     System.out.println ("Connected.");



//     //set up streams
//     OutputStream out = socket.getOutputStream ();
//     ObjectOutputStream ous = new ObjectOutputStream( out );
//     InputStream in = socket.getInputStream ();
//     ObjectInputStream ins = new ObjectInputStream (in);

//     //make 2 accounts
//     ous.writeObject( new NewAccountRequest("John", "Doe", "333 Maple Road"));
//     ous.writeObject( new NewAccountRequest("Fred", "Doe", "111 Chestnut Street"));

//     int aid_1 = 0, aid_2 = 0, bal1 = 0, bal2 = 0;
//     String status1, status2;
//     Response newRes1, newRes2;

//     //get accountIDs back;keep track of them
//     try {
//         newRes1= (Response) ins.readObject();
//         newRes2 = (Response) ins.readObject();
//         aid_1 = newRes1.getAccountID();
//         aid_2 = newRes2.getAccountID();
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //deposit 100s
//     ous.writeObject( new DepositRequest(aid_1, 100));
//     ous.writeObject( new DepositRequest(aid_2, 100));
//     try {
//         newRes1 = (Response) ins.readObject();
//         newRes2 = (Response) ins.readObject();
//         status1 = newRes1.getStatus();
//         status2 = newRes2.getStatus();
//         System.out.println("Statuses: "+status1+ " " +status2);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //get account balances
//     ous.writeObject( new GetBalanceRequest(aid_1));
//     ous.writeObject( new GetBalanceRequest(aid_2));
//     try {
//         newRes1 = (Response) ins.readObject();
//         newRes2 = (Response) ins.readObject();
//         bal1 = newRes1.getBalance();
//         bal2 = newRes2.getBalance();
//         System.out.println("Balances: "+bal1+ " " + bal2);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //transfer, read
//     ous.writeObject( new TransferRequest (aid_1, aid_2, 100));
//     try {
//         newRes1 = (Response) ins.readObject();
//         status1 = newRes1.getStatus();
//         System.out.println("Status of first transfer attempt: "+status1);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //get balances again
//     ous.writeObject( new GetBalanceRequest(aid_1));
//     ous.writeObject( new GetBalanceRequest(aid_2));
//     try {
//         newRes1 = (Response) ins.readObject();
//         newRes2 = (Response) ins.readObject();
//         bal1 = newRes1.getBalance();
//         bal2 = newRes2.getBalance();
//         System.out.println("Balances again: "+bal1+ " " + bal2);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //attempt to Withdraw 100s
//     ous.writeObject( new WithdrawRequest(aid_1, 100));
//     ous.writeObject( new WithdrawRequest(aid_2, 100));
//     try {
//         newRes1 = (Response) ins.readObject();
//         newRes2 = (Response) ins.readObject();
//         status1 = newRes1.getStatus();
//         status2 = newRes2.getStatus();
//         System.out.println("Post-withdraw statuses: "+status1+ " " +status2);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //attempt to transfer again
//     ous.writeObject( new TransferRequest (aid_1, aid_2, 100));
//     try {
//         newRes1 = (Response) ins.readObject();
//         status1 = newRes1.getStatus();
//         System.out.println("Status after second transfer: "+status1);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//     //get balances
//     ous.writeObject( new GetBalanceRequest(aid_1));
//     ous.writeObject( new GetBalanceRequest(aid_2));
//     try {
//         newRes1 = (Response) ins.readObject();
//         newRes2 = (Response) ins.readObject();
//         bal1 = newRes1.getBalance();
//         bal2 = newRes2.getBalance();
//         System.out.println("Final balances: "+bal1+ " " + bal2);
//     }
//     catch ( ClassNotFoundException e) {
//          e.printStackTrace();
//     }

//   }
// }
