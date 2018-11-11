package Company.Behaviors;

import Common.Date;
import Company.Company;
import Company.Manager;
import jade.core.behaviours.Behaviour;

public class ExecuteManagerPayoutBehavior extends Behaviour
{
    private Company company;
    private boolean done = false;

    public ExecuteManagerPayoutBehavior(Company company)
    {
        this.company = company;
    }

    @Override
    public void action()
    {
        if(Date.CURRENT_DATE.getDay()==1)
        {
            for(Manager manager:this.company.getManagers())
            {
                company.reduceCapital(manager.getSalary());
                manager.addMoney(manager.getSalary());
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
