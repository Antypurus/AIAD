package Components;

public class Product
{

    private double price;
    private double profit;

    public Product(double price,double profit)
    {
        this.price = price;
        this.profit = profit;
    }

    public double getPrice()
    {
        return this.price;
    }

    public double getProfit()
    {
        return this.profit;
    }

}
