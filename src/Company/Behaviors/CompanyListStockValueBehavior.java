package Company.Behaviors;

import Common.Date;
import Company.Agents.CompanyAgent;
import jade.core.behaviours.Behaviour;

public class CompanyListStockValueBehavior extends Behaviour
{

    private boolean done = false;
    private CompanyAgent agent;

    public CompanyListStockValueBehavior(CompanyAgent agent)
    {
        this.agent = agent;
    }

    @Override
    public void action()
    {
        System.out.println(this.agent.getCompany().getIndex().getName() + " :: " + Date.CURRENT_DATE +
                " :: " + this.agent.getCompany().getAcronym() + " -> " + this.agent.getCompany().getStockValue().getStockValue());
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}