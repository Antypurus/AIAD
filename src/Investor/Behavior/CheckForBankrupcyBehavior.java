package Investor.Behavior;

import Aggregators.Index;
import Company.Company;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;

public class CheckForBankrupcyBehavior extends Behaviour
{
    private boolean done = false;
    private Investor investor;
    private Index index;

    @Override
    public void action()
    {
        if(this.investor.getPortfolio().size()!=0)
        {
            this.done = true;
            return;
        }

        ArrayList<Company> companies = this.index.getAllCompanies();
        for(Company company:companies)
        {
            if(this.investor.getCurrentMoney()>=company.getStockValue().getStockValue())
            {
                this.done = true;
                return;
            }

            if(this.investor.getReserverMoney()>=company.getStockValue().getStockValue())
            {
                this.done = true;
                return;
            }
        }

        this.investor.getAgent().doDelete();//kills the agent
        System.out.println("Investor "+this.investor.getName()+" has gone " +
                "bankrupt and can no longer invest money");
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
