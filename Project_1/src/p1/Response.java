package p1;

public class Response implements java.io.Serializable{
    public String type;

    Response(String type){
    	this.type = type;
    }

    String getType(){
    	return this.type;
    }

    public int getAccountID(){
    	return 0;
    }
    public String getStatus(){
    	return "INVALID";
    }
    public int getBalance(){
    	return 0;
    }
}

class NewAccountResponse extends Response{
	public int accountID;

	NewAccountResponse(int accountID){
		super("NewAccountResponse");
		this.accountID = accountID;
	}

	public int getAccountID(){
    	return this.accountID;
    }
}
class DepositResponse extends Response{
	public String status;

	DepositResponse(String status){
		super("DepositResponse");
		this.status = status;
	}

	public String getStatus(){
    	return this.status;
    }
	
}
class WithdrawResponse extends Response{
	public String status;

	WithdrawResponse(String status){
		super("WithdrawResponse");
		this.status = status;
	}

	public String getStatus(){
    	return this.status;
    }
}
class GetBalanceResponse extends Response{
	public int balance;

	GetBalanceResponse(int balance){
		super("GetBalanceResponse");
		this.balance = balance;
	}

	public int getBalance(){
    	return this.balance;
    }
}
class TransferResponse extends Response{
	public String status;

	TransferResponse(String status){
		super("TransferResponse");
		this.status = status;
	}

	public String getStatus(){
    	return this.status;
    }
	
}






