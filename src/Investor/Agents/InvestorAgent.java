package Investor.Agents;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Investor.Behavior.CheckForBankrupcyBehavior;
import Investor.Behavior.FindCompanyToInvestBehavior;
import Investor.Investor;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class InvestorAgent extends Agent
{

    private Investor investor;
    private Index index;
    private InvestorAgency agency;

    public void setup()
    {
        Object[] args = getArguments();

        String name = args[0].toString();
        double riskBias = (Double) args[1];
        this.agency = (InvestorAgency) args[2];
        double startingMoney = (Double) args[3];

        this.investor = new Investor(name, riskBias, this.agency, startingMoney,
                this);

        this.index = (Index) args[4];

        this.addBehaviour(new IntroductionBehavior());
    }

    public Investor getInvestor()
    {
        return this.investor;
    }

    class IntroductionBehavior extends Behaviour
    {

        private boolean done = false;

        @Override
        public void action()
        {
            String name =
                    ((InvestorAgent) (this.myAgent)).getInvestor().getName();
            System.out.println("Investor " + name + " Has Entered the Battlefield");
            this.done = true;
        }

        @Override
        public boolean done()
        {
            return this.done;
        }
    }

    public void newDayProtocol()
    {
        this.addBehaviour(new CheckForBankrupcyBehavior(this.investor, this.index));
        this.addBehaviour(new FindCompanyToInvestBehavior(this.investor,
                this.index, this.agency));
    }

}
