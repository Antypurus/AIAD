package Company.Behaviors;

import Company.Company;
import Components.Product;
import jade.core.behaviours.Behaviour;

import java.util.Random;

public class CheckSalesBehavior extends Behaviour
{
    private Company company;
    private boolean done = false;

    private static final int MAX_DAILY_SALES = 1000;

    public CheckSalesBehavior(Company company)
    {
        this.company = company;
    }

    @Override
    public void action()
    {
        for(Product product:this.company.getProducts())
        {
            for(int i=0;i<MAX_DAILY_SALES;++i)
            {
                boolean shouldbuy = this.shouldBuy(product);
                if(shouldbuy)
                {
                    this.executeBuy(product);
                }
            }
        }
        this.done = true;
    }

    private boolean shouldBuy(Product product)
    {
        double buyProb =
                (product.getPrice()*product.getQuality())/product.getPrice();
        Random rand = new Random();
        double gamma = rand.nextDouble();
        if(gamma<=buyProb)
        {
            return true;
        }
        return false;
    }

    private void executeBuy(Product product)
    {
        this.company.addCapital(product.getPrice()*product.getProfit());
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
