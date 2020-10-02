package my.edu.tarc.finalyearproject.Domain;


public class Transaction {
    private String transactionID;
    private String transactionDesc;
    private String transactionType;
    private double transactionAmt;
    private String datetime;

    public Transaction(String transactionID, String transactionDesc, String transactionType, double transactionAmt, String datetime) {
        this.transactionID = transactionID;
        this.transactionDesc = transactionDesc;
        this.transactionType = transactionType;
        this.transactionAmt = transactionAmt;
        this.datetime = datetime;
    }
    public  Transaction(){

    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getTransactionAmt() {
        return transactionAmt;
    }

    public void setTransactionAmt(double transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
