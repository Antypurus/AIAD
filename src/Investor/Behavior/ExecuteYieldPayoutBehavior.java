package Investor.Behavior;

import Common.Date;
import Company.Company;
import Components.Stock;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

public class ExecuteYieldPayoutBehavior extends Behaviour
{
    private Investor investor;
    private boolean done = false;

    public ExecuteYieldPayoutBehavior(Investor investor)
    {
        this.investor = investor;
    }

    @Override
    public void action()
    {
        if (Date.CURRENT_DATE.getDay() == 1 && Date.CURRENT_DATE.getMonth() == 1)
        {
            for (Stock stock : this.investor.getPortfolio())
            {
                Company company = stock.getCompany();
                double yield = stock.getYield();
                int ammount = stock.getShareCount();

                double payout =
                        yield * ammount * company.getStockValue().getStockValue();
                company.reduceCapital(payout);
                this.investor.addMoney(payout);
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
