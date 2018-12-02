package Investor.Agents;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import DataExporter.EventLogger.EventLogger;
import DataExporter.EventLogger.Events.InvestorCapitalEvent;
import Investor.Behavior.CheckForBankruptcyBehavior;
import Investor.Behavior.ExecuteYieldPayoutBehavior;
import Investor.Behavior.FindCompanyToInvestBehavior;
import Investor.Behavior.HandleBuyRequestBehavior;
import Investor.Investor;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class InvestorAgent extends Agent
{

    private Investor investor;
    private Index index;
    private InvestorAgency agency;

    private int slot;

    private boolean isInvesting = false;

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

        this.slot = EventLogger.get_slot();

        this.addBehaviour(new IntroductionBehavior());
        this.addBehaviour(new HandleBuyRequestBehavior(this, this.index));
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
        this.addBehaviour(new CheckForBankruptcyBehavior(this.investor, this.index));
        this.addBehaviour(new FindCompanyToInvestBehavior(this.investor,
                this.index, this.agency));
        this.addBehaviour(new ExecuteYieldPayoutBehavior(this.investor));

        EventLogger.register_event(new InvestorCapitalEvent(this.investor.getName(),this.investor.getRiskBiasFactor(),this.investor.getCapitalValue()),
                Date.CURRENT_DATE,this.slot);
    }

    public void isCurrentlyInvesting()
    {
        this.isInvesting = true;
    }

    public void isNotInvesting()
    {
        this.isInvesting = false;
    }

    public boolean isInvesting()
    {
        return this.isInvesting;
    }

    public Index getIndex()
    {
        return this.index;
    }
}