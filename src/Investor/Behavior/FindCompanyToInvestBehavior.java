package Investor.Behavior;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import Company.Company;
import Investor.Agents.InvestorAgent;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class FindCompanyToInvestBehavior extends Behaviour
{

    private Index index;
    private InvestorAgent agent;
    private boolean done = false;
    private InvestorAgency agency;

    private static final int MAX_ROUNDS = 5;

    public FindCompanyToInvestBehavior(Investor investor, Index index,
                                       InvestorAgency agency)
    {
        this.index = index;
        this.agent = investor.getAgent();
        this.agency = agency;
    }

    @Override
    public void action()
    {
        //if (!(this.agent.isInvesting()))
        {
            CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();
            int numberOfCompanies = companies.size();
            Random rand = new Random();

            int roundCounter = 0;
            Company investIn = null;

            if (numberOfCompanies != 0)
            {
                while (investIn == null && roundCounter <= MAX_ROUNDS)
                {
                    int index = rand.nextInt() % numberOfCompanies;
                    if (companies.get(index).getStockValue().getStockValue() <= this.agent.getInvestor().getCurrentMoney())
                    {
                        investIn = companies.get(index);
                    } else
                    {
                        roundCounter++;
                    }
                }
            }

            if (investIn != null)
            {
                System.out.println(Date.CURRENT_DATE + " :: " + this.agent.getInvestor().getName() + " has " +
                        "decided to buy stock from " + investIn.getName() +
                        " Currently have $" + this.agent.getInvestor().getCurrentMoney() + " to invest");

                this.agent.addBehaviour(new FindStockSourceBehavior(investIn,
                        this.agency,
                        this.agent.getInvestor()));
            } else
            {
                System.out.println(Date.CURRENT_DATE + " :: " + this.agent.getInvestor().getName() + " is currently unable to invest in any company");
            }
        } //else
        {
           // System.out.println(Date.CURRENT_DATE + " :: " + this.agent
            // .getInvestor().getName() + " is currently investing and cannot start investing in another company");
        }
        done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
