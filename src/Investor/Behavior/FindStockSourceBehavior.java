package Investor.Behavior;

import Aggregators.InvestorAgency;
import Common.Date;
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
            //nobody has bought need to buy from company if available
            if (this.target.getStock().getShareCount() != 0)
            {
                //need to buy from the company
                System.out.println(Date.CURRENT_DATE + " :: " + this.investor.getName() + " " +
                        "Will try to buy " +
                        "stock directly from " + this.target.getName());
                this.investor.getAgent().addBehaviour(new BuyStockFromCompanyBehavior(this.investor, this.target));
            }
        } else
        {
            // for when the only investor with stock is ourselves
            if (investors.size() == 1)
            {
                if (investors.get(0) == this.investor)
                {
                    System.out.println(Date.CURRENT_DATE + " :: " + this.investor.getName() + " " +
                            "Will try to buy " +
                            "stock directly from " + this.target.getName());
                    this.investor.getAgent().addBehaviour(new BuyStockFromCompanyBehavior(this.investor, this.target));
                }
            }
            //need to start negotiating with these motherfuckers
            CopyOnWriteArrayList<Investor> stockSources =
                    new CopyOnWriteArrayList<>(investors);
            this.investor.getAgent().addBehaviour(new BuyStockFromAgentBehavior(this.investor, stockSources,this.target));
        }

        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
