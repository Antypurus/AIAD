package Investor.Agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class InvestorAgent extends Agent {

    private String name;

    public void setup() {
        Object[] args = getArguments();
        this.name = args[0].toString();

        System.out.println("Hello " + this.name);
    }

    public class InvestmentBehavior extends Behaviour {

        @Override
        public void action() {

        }

        @Override
        public boolean done() {
            return true;
        }
    }

}
