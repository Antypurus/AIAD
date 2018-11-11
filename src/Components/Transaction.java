package Components;

import Common.Date;
import Investor.Investor;

public class Transaction
{

    private Stock stock;
    private Investor seller;
    private Investor buyer;
    private int amount;
    private double shareValue;
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

      try {
        this.buyer.removeMoney(newValue);
        this.seller.addMoney(newValue);

        this.buyer.registerStock(this.stock.split(this.amount));
      }
      catch (Exception e) {
          e.printStackTrace();
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
