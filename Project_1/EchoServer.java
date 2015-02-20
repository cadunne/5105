import java.net.*;
import java.io.*;
// import java.lang.Thread;

public class EchoServer extends Thread {
  protected Socket s;
  protected FileWriter fileLog;

  EchoServer (Socket s) {
    System.out.println ("New client.");
    this.s = s;
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

    if (args.length != 1)
         throw new RuntimeException ("Syntax: EchoServer port-number");

    System.out.println ("Starting on port " + args[0]);
    ServerSocket server = new ServerSocket (Integer.parseInt (args[0]));

    while (true) {
      System.out.println ("Waiting for a client request");
      Socket client = server.accept ();
      System.out.println ("Received request from " + client.getInetAddress ());
      EchoServer c = new EchoServer (client);
      c.start ();
    }
  }
}
