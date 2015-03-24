package p1;

//Abstract
public class Request implements java.io.Serializable{
    public String type;

    Request(String type){
    	this.type = type;
    }

    String getType(){
    	return this.type;
    }

    public String getFirstName(){
    	return "INVALID";
    }
    public String getLastName(){
    	return "INVALID";
    }
    public String getAddress(){
    	return "INVALID";
    }
    public int getAccountID(){
    	return 0;
    }
    public int getTargetID(){
    	return 0;
    }
    public int getAmount(){
    	return 0;
    }
}


class NewAccountRequest extends Request{
	public String firstName, lastName, address;

	NewAccountRequest(String firstName, String lastName, String address){
		super("NewAccountRequest");
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
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

	public String toString() { 
	    return "Name: " + this.firstName + this.lastName + this.address + "\n";
	}
}
class DepositRequest extends Request{
	public int accountID, amount;

	DepositRequest(int accountID, int amount){
		super("DepositRequest");
		this.accountID = accountID;
		this.amount = amount;
	}

	public int getAccountID(){
    	return this.accountID;
    }
    public int getAmount(){
    	return this.amount;
    }


	
}
class WithdrawRequest extends Request{
	public int accountID, amount;

	WithdrawRequest(int accountID, int amount){
		super("WithdrawRequest");
		this.accountID = accountID;
		this.amount = amount;
	}

	public int getAccountID(){
    	return this.accountID;
    }
    public int getAmount(){
    	return this.amount;
    }
}
class GetBalanceRequest extends Request{
	public int accountID;

	GetBalanceRequest(int accountID){
		super("GetBalanceRequest");
		this.accountID = accountID;
	}

	public int getAccountID(){
    	return this.accountID;
    }
}
class TransferRequest extends Request{
	public int accountID, targetID, amount;

	TransferRequest(int accountID, int targetID, int amount){
		super("TransferRequest");
		this.accountID = accountID;
		this.targetID = targetID;
		this.amount = amount;
	}

	public int getAccountID(){
    	return this.accountID;
    }
    public int getTargetID(){
    	return this.targetID;
    }
    public int getAmount(){
    	return this.amount;
    }
	
}





