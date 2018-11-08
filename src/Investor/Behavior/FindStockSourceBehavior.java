package Investor.Behavior;

import Aggregators.InvestorAgency;
import Company.Company;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

public class FindStockSourceBehavior extends Behaviour
{

    private Company target;
    private InvestorAgency agency;
    private Investor investor;
    private boolean done = false;

    public FindStockSourceBehavior(Company target,InvestorAgency agency,
                                       Investor investor)
    {
        this.target = target;
        this.agency = agency;
        this.investor = investor;
    }

    @Override
    public void action()
    {

    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
