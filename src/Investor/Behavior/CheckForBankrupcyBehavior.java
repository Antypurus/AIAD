package Investor.Behavior;

import Aggregators.Index;
import Common.Date;
import Company.Company;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

import java.util.concurrent.CopyOnWriteArrayList;

public class CheckForBankrupcyBehavior extends Behaviour
{
    private boolean done = false;
    private Investor investor;
    private Index index;

    public CheckForBankrupcyBehavior(Investor investor, Index index)
    {
        this.investor = investor;
        this.index = index;
    }

    @Override
    public void action()
    {
        System.out.println(Date.CURRENT_DATE+" Started Bankruptcy check for" +
          " "+this.investor.getName());

        if (this.investor.getPortfolio().size() != 0)
        {
            this.done = true;
            return;
        }

        CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();
        for (Company company : companies)
        {
            if (this.investor.getCurrentMoney() >= company.getStockValue().getStockValue())
            {
                this.done = true;
                return;
            }

            if (this.investor.getReserverMoney() >= company.getStockValue().getStockValue())
            {
                this.done = true;
                return;
            }
        }

        if (companies.size() > 0)
        {
            this.investor.getAgent().doDelete();//kills the agent
            System.out.println("Investor " + this.investor.getName() + " has gone " +
                    "bankrupt and can no longer invest money");
        }
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
