package Investor.Agents;

import Aggregators.InvestorAgency;
import Investor.Investor;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.concurrent.CopyOnWriteArrayList;

public class InvestorAgent extends Agent {

    private String name;

    public void setup() {
        Object[] args = getArguments();
        this.name = args[0].toString();
        System.out.println("Hello " + this.name);

        InvestorAgency agency = (InvestorAgency) args[1];
        Investor investor = new Investor(name,0.5,agency,5000,this);

        this.addBehaviour(new ListeningBehavior());
        this.addBehaviour(new SendBehavior(agency.getInvestors(),this));
    }

    public class Test extends ContractNetInitiator
    {

        public Test(Agent a, ACLMessage cfp) {
            super(a, cfp);
        }

    }

    public class ListeningBehavior extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage msg = receive();
            if(msg != null) {
                System.out.println("----------------------------");
                System.out.println("RECEIVER:"+this.myAgent.getName());
                System.out.println("SENDER:"+msg.getSender().getName());
                System.out.println("MESSAGE:"+msg.getContent());
                System.out.println("----------------------------");
            } else {
                block();
            }
        }

    }

    public class SendBehavior extends Behaviour
    {
        private CopyOnWriteArrayList<Investor> investors = null;
        private boolean done = false;
        private InvestorAgent agent;

        public SendBehavior(CopyOnWriteArrayList<Investor> investors, InvestorAgent agent)
        {
            this.investors = (CopyOnWriteArrayList<Investor>)investors.clone();
            this.agent = agent;
        }

        @Override
        public void action() {
            for(Investor investor:this.investors)
            {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(investor.getAgent().getAID());
                msg.setContent("Hello");
                this.agent.send(msg);
            }
            this.done = true;
        }

        @Override
        public boolean done() {
            return this.done;
        }
    }

}
