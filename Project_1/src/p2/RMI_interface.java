package p2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_Interface extends Remote {
  
  public int makeNewAccount(String firstName, String lastName, String address)
  	throws RemoteException;

  public String deposit(int accountID, int amount)
  	throws RemoteException;

  public String withdraw(int accountID, int amount)
  	throws RemoteException;

  public int getBalance(int accountID)
  	throws RemoteException;

  public String transfer(int accountID, int targetID, int amount)
  	throws RemoteException;

}

