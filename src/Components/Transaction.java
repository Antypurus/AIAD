package Components;

import Common.Date;
import Investor.Investor;

public class Transaction {

    private Stock stock;
    private Investor seller;
    private Investor buyer;
    private int ammount;
    private double shareValue;
    private Date transactionDate;

    public Transaction() {

    }

    public Stock getStock()
    {
        return this.stock;
    }

    public double getShareValue()
    {
        return this.shareValue;
    }

    public int getAmmount()
    {
        return this.ammount;
    }

}
