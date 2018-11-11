package Components;

public class Product
{

    private double price;
    private double profit;
    private double quality;

    public Product(double price, double profit, double quality)
    {
        this.price = price;
        this.profit = profit;
        this.quality = quality;
    }

    public double getPrice()
    {
        return this.price;
    }

    public double getProfit()
    {
        return this.profit;
    }

    public double getQuality()
    {
        return this.quality;
    }

}
