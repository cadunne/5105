import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Hashtable;

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

    System.out.println("Size: "+ int_to_string.size());
    int_to_string.put(1, "one");
    System.out.println("Size: "+ int_to_string.size());

 

    try {
      InputStream istream = s.getInputStream ();
      OutputStream ostream = s.getOutputStream ();
      PrintWriter outp = new PrintWriter (ostream, true);
      outp.println ("Welcome to the multithreaded echo server."); //not being written right away
      byte buffer[] = new byte[16];
      int read;
      while ((read = istream.read (buffer)) >= 0) {
        ostream.write (buffer, 0, read);
        System.out.write (buffer, 0, read);
        System.out.flush();
      }
      System.out.println ("Client exit.");
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
