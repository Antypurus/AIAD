package Components;

public class StockValue
{

    private double stockValue;

    public StockValue(double stockValue)
    {
        this.stockValue = stockValue;
    }

    public synchronized double getStockValue()
    {
        return this.stockValue;
    }

    public synchronized void setStockValue(double stockValue)
    {
        this.stockValue = stockValue;
    }
}
