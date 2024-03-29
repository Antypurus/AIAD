package Investor.Agents;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import DataExporter.EventLogger.EventLogger;
import DataExporter.EventLogger.Events.InvestorCapitalEvent;
import DataExporter.EventLogger.SlotType;
import Investor.Behavior.*;
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

        this.slot = EventLogger.get_slot(SlotType.InvestorSlot);

        this.addBehaviour(new IntroductionBehavior());
        this.addBehaviour(new AgentReceiverDispatcher(this, this.index));
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
        this.investor.stockSync();
        //this.addBehaviour(new CheckForBankruptcyBehavior(this.investor,
         //   this.index));
        this.addBehaviour(new FindCompanyToInvestBehavior(this.investor,
                this.index, this.agency));
        this.addBehaviour(new ExecuteYieldPayoutBehavior(this.investor));
        this.investor.register_capital(Date.CURRENT_DATE.getPreviousDay(),
                investor.getCapitalValue());

        this.addBehaviour(new AgentReceiverDispatcher(this, this.index));

        EventLogger.register_event(new InvestorCapitalEvent(this.investor.getName(),this.investor.getRiskBiasFactor(),this.investor.getCapitalValue(),this.investor.get_week_capital_delta(),this.agency,this.index),
                Date.CURRENT_DATE.getPreviousDay(),this.slot,SlotType.InvestorSlot);
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