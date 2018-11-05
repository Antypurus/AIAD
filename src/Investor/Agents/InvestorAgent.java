package Investor.Agents;

import Aggregators.InvestorAgency;
import Investor.Investor;
import jade.core.Agent;

public class InvestorAgent extends Agent {

    private String name;

    public void setup() {
        Object[] args = getArguments();
        this.name = args[0].toString();
        System.out.println("Hello " + this.name);

        InvestorAgency agency = (InvestorAgency) args[1];
        Investor investor = new Investor(name,0.5,agency,5000,this);
    }



}
