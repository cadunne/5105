package p1;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Hashtable;

import p1.Request;
import p1.Account;


public class EchoServer extends Thread {
  protected Socket s;
  protected FileWriter fileLog;
  protected Hashtable<Integer, String> int_to_string; //change to store better data

  EchoServer (Socket s, Hashtable<Integer, String> ht) {
    System.out.println ("New client.");
    this.s = s;
    this.int_to_string = ht;
    try{
      this.fileLog = new FileWriter("MyFile.txt", true);
    }
    catch(IOException ioe){
        System.err.println("IOException: " + ioe.getMessage());
    }
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
      ObjectInputStream oin = new ObjectInputStream (istream); //NEW
      OutputStream ostream = s.getOutputStream ();
      PrintWriter outp = new PrintWriter (ostream, true);
      outp.println ("Welcome to the multithreaded echo server."); //not being written right away

      Request newReq = null;

      // Date date = null;
      // String name = null;
      try {
        newReq = (Request) oin.readObject();
        System.out.println(newReq.getType());
        System.out.println(newReq);

         // date = (Date) oin.readObject();
         // name = (String) oin.readObject();
      }
      catch ( ClassNotFoundException e) {
         e.printStackTrace();
      }

      // System.out.println( "Name: "+  name+  ", Date: "+ date );


      /*
      byte buffer[] = new byte[16];
      int read;
      while ((read = istream.read (buffer)) >= 0) {
        ostream.write (buffer, 0, read);

        String s = new String(buffer).replaceAll(" ","");
        System.out.println("new string got: " + s);
        // System.out.write (buffer, 0, read);
        int_to_string.put(s.hashCode(), s);

        // System.out.flush();
      }
      System.out.println ("Client exit.");
      System.out.println("Hash table: " + int_to_string);
      */
    } catch (IOException ex) {
      ex.printStackTrace ();
    } finally {
      try {
        s.close ();
      } catch (IOException ex) {
        ex.printStackTrace ();
      }
    }
  }

  public static void main (String args[]) throws IOException {

    //delete file log to start new one.

    Request req = new GetBalanceRequest(55);
    System.out.println(req.getType() + "\n");

    Account acc = new Account(155, 0, "FN", "LN", "addrrrrr");
    System.out.println(acc);

    Hashtable<Integer, String> newHt = new Hashtable<Integer, String>();

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
