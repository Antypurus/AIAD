package Company.Behaviors;

import Common.Date;
import Company.Company;
import Components.Product;
import jade.core.behaviours.Behaviour;

import java.util.Random;

public class ProductLaunchBehavior extends Behaviour
{
    private Company company;
    private boolean done = false;

    private static final double LAUNCH_PROBABILITY = 0.25;
    private static final double MAX_PRICE = 1000.0;
    private static final double MAX_PROFIT = 0.5;
    private static final double QUALITY_DELTA = 0.3;

    public ProductLaunchBehavior(Company company)
    {
        this.company = company;
    }

    @Override
    public void action()
    {
        if (Date.CURRENT_DATE.getDay() == 1)
        {
            Random rand = new Random();

            double launch = rand.nextDouble();
            boolean shouldLaunch = false;
            if (launch <= LAUNCH_PROBABILITY)
            {
                shouldLaunch = true;
            }

            if (shouldLaunch)
            {
                double productQualityDelta = rand.nextDouble();
                double productQuality = 0;
                if (productQualityDelta <= this.company.getQualityBias())
                {
                    double delta = rand.nextDouble() % QUALITY_DELTA;
                    productQuality = (1 + delta) * company.getQualityBias();
                } else
                {
                    double delta = rand.nextDouble() % QUALITY_DELTA;
                    productQuality = (1 - delta) * company.getQualityBias();
                }

                double price = rand.nextDouble()*MAX_PRICE;
                double profit = rand.nextDouble()*MAX_PROFIT;

                System.out.println(company.getName()+" Has released a product");
                this.company.addProduct(new Product(price,profit,productQuality));
            }
        }
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
