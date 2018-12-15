package Components;

import Common.Date;
import Investor.Investor;

public class Transaction
{

    private Stock stock;
    private Investor seller;
    private Investor buyer;
    private volatile int amount;
    private volatile double shareValue;
    private Date transactionDate;

    public Transaction(Stock stock, Investor seller, Investor buyer, int amount, double shareValue, Date transactionDate)
    {
        this.stock = stock;
        this.seller = seller;
        this.buyer = buyer;
        this.amount = amount;
        this.shareValue = shareValue;
        this.transactionDate = transactionDate;
    }

    public void executeTransaction()
    {
        double newValue = this.shareValue * this.amount;

        if (this.stock != null)
        {
            try
            {
                this.buyer.removeMoney(newValue);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            this.seller.addMoney(newValue);

            Stock newStock = this.stock.split(this.amount);
            this.buyer.registerStock(newStock);
        }
    }

    public Stock getStock()
    {
        return this.stock;
    }

    public Investor getSeller()
    {
        return this.seller;
    }

    public Investor getBuyer()
    {
        return this.buyer;
    }

    public int getAmount()
    {
        return this.amount;
    }

    public double getShareValue()
    {
        return this.shareValue;
    }

    public Date getTransactionDate()
    {
        return this.transactionDate;
    }
}
