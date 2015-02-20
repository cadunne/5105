package p1;

public class Request {
    public String type;

    Request(String type){
    	this.type = type;
    }

    String getType(){
    	return this.type;
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
	
}
class DepositRequest extends Request{
	public int accountID, amount;

	DepositRequest(int accountID, int amount){
		super("DepositRequest");
		this.accountID = accountID;
		this.amount = amount;
	}
	
}
class WithdrawRequest extends Request{
	public int accountID, amount;

	WithdrawRequest(int accountID, int amount){
		super("WithdrawRequest");
		this.accountID = accountID;
		this.amount = amount;
	}
}
class GetBalanceRequest extends Request{
	public int accountID;

	GetBalanceRequest(int accountID){
		super("GetBalanceRequest");
		this.accountID = accountID;
	}
}
class TransferRequest extends Request{
	public int myID, targetID, amount;

	TransferRequest(int myID, int targetID, int amount){
		super("TransferRequest");
		this.myID = myID;
		this.targetID = targetID;
		this.amount = amount;
	}
	
}





