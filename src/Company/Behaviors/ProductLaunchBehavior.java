package Company.Behaviors;

import Common.Date;
import Company.Company;
import jade.core.behaviours.Behaviour;

public class ProductLaunchBehavior extends Behaviour
{
    private Company company;
    private boolean done = false;

    public ProductLaunchBehavior(Company company)
    {
        this.company = company;
    }

    @Override
    public void action()
    {
        if (Date.CURRENT_DATE.getDay() == 1)
        {

        }
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
