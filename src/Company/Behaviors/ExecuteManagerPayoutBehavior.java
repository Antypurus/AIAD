package Company.Behaviors;

import Company.Company;
import jade.core.behaviours.Behaviour;

public class ExecuteManagerPayoutBehavior extends Behaviour
{
    private Company company;

    public ExecuteManagerPayoutBehavior(Company company)
    {
        this.company = company;
    }

    @Override
    public void action()
    {

    }

    @Override
    public boolean done()
    {
        return false;
    }
}
