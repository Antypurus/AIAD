package Investor.Behavior;

import Aggregators.InvestorAgency;
import Company.Company;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

import java.util.concurrent.CopyOnWriteArrayList;

public class FindStockSourceBehavior extends Behaviour
{

    private Company target;
    private InvestorAgency agency;
    private Investor investor;
    private boolean done = false;

    public FindStockSourceBehavior(Company target, InvestorAgency agency,
                                   Investor investor)
    {
        this.target = target;
        this.agency = agency;
        this.investor = investor;
    }

    @Override
    public void action()
    {
        CopyOnWriteArrayList<Investor> investors =
                this.agency.getInvestorWithStockOfCompany(target);

        if (investors == null)
        {
            //nobody has bough need to buy from company if available
            if (this.target.getStock().getShareCount() != 0)
            {
                //need to buy from the company
                this.investor.getAgent().addBehaviour(new BuyStockFromCompanyBehavior(this.investor, this.target));
            }
        } else
        {
            //need to start negotiating with these motherfuckers
        }

        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
