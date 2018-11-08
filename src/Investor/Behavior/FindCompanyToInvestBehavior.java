package Investor.Behavior;

import Aggregators.Index;
import Aggregators.InvestorAgency;
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

    FindCompanyToInvestBehavior(Investor investor, Index index,
                                InvestorAgency agency) throws Exception
    {
        this.index = index;
        this.agent = investor.getAgent();
        this.agency = agency;
    }

    @Override
    public void action()
    {
        CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();
        int numberOfCompanies = companies.size();
        Random rand = new Random();

        int roundCounter = 0;
        Company investIn = null;
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

        if (investIn != null)
        {
            this.agent.addBehaviour(new FindStockSourceBehavior(investIn,
                    this.agency,
                    this.agent.getInvestor()));
        }

        done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
