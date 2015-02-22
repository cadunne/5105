package p2;

public class Account{
	private int accountID, balance;
	private String firstName, lastName, address;

    Account(int accountID, int balance, String firstName, String lastName, String address){
    	this.accountID = accountID;
    	this.balance = balance;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.address = address;
    }

    public String toString() { 
	    return "Account info: " + this.accountID + "" + this.balance + this.firstName + this.lastName + this.address + "\n";
	}

    public int getAccountID(){
        return this.accountID;
    }
    public int getBalance(){
        return this.balance;
    }
    public void setBalance(int newBal){
        this.balance = newBal;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getAddress(){
        return this.address;
    }
}
