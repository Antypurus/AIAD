package Investor.Behavior;

import Company.Company;
import Components.Stock;
import Investor.Agents.InvestorAgent;
import Investor.Investor;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

public class BuyFromCompanyRequestBehavior extends AchieveREInitiator
{
    private ACLMessage message;

    private Investor investor;
    private Company company;
    private int ammount;

    public BuyFromCompanyRequestBehavior(Agent a, ACLMessage msg)
    {
        super(a, msg);

        this.message = msg;

        if(!(a instanceof InvestorAgent))
        {
            System.out.println("GTFO");
        }
    }

    public void target(Investor investor, Company company,int ammount)
    {
        this.investor = investor;
        this.company = company;
        this.ammount = ammount;
    }

    @Override
    protected java.util.Vector	prepareRequests(ACLMessage request)
    {
        Vector vector = new Vector();
        vector.add(this.message);
        return vector;
    }

    @Override
    protected void	handleAgree(ACLMessage agree)
    {
        Stock newStock = this.company.getStock().split(this.ammount);
        this.investor.registerStock(newStock);
        double saleValue = newStock.getTotalValue();
        try
        {
            this.investor.removeMoney(saleValue);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        this.company.addCapital(saleValue);
    }
}
