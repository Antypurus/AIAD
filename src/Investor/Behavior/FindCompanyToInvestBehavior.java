package Investor.Behavior;

import Aggregators.Index;
import Company.Company;
import Investor.Agents.InvestorAgent;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Random;

public class FindCompanyToInvestBehavior extends Behaviour {

    private Index index;
    private InvestorAgent agent;
    private boolean done = false;

    private static final int MAX_ROUNDS = 5;

    FindCompanyToInvestBehavior(Index index) throws Exception {
        this.index = index;
        if(!(this.myAgent instanceof InvestorAgent))
        {
            throw new Exception("Invalid Agent, this behavior only supports " +
                    "Investor agents");
        }
        this.agent = (InvestorAgent)this.myAgent;
    }

    @Override
    public void action() {
        ArrayList<Company>companies = this.index.getAllCompanies();
        int numberOfCompanies = companies.size();
        Random rand = new Random();

        int roundCounter = 0;
        Company investIn = null;
        while(investIn == null && roundCounter<=MAX_ROUNDS)
        {
            int index = rand.nextInt()%numberOfCompanies;
            if(companies.get(index).getStockValue().getStockValue()<=this.agent.getInvestor().getCurrentMoney())
            {
                investIn = companies.get(index);
            }
            else
            {
                roundCounter++;
            }
        }

        if(investIn!=null)
        {
            //add behavior to try to invest in this company
        }

        done = true;
    }

    @Override
    public boolean done() {
        return this.done;
    }
}
