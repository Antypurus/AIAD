package Investor.Behavior;

import Common.Date;
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
    private int amount;

    public BuyFromCompanyRequestBehavior(Agent a, ACLMessage msg)
    {
        super(a, msg);

        this.message = msg;

        if(!(a instanceof InvestorAgent))
        {
            System.out.println("Error: Not an Investor Agent");
        }
    }

    public void target(Investor investor, Company company,int amount)
    {
        this.investor = investor;
        this.company = company;
        this.amount = amount;
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
        Stock newStock = this.company.getStock().split(this.amount);
        System.out.println(Date.CURRENT_DATE+" :: "+this.investor.getName()+
                " has bought "+ amount +" stocks directly from "+company.getName()+" for $"+newStock.getTotalValue());
        //this here is important for consistency when registering
        // a stock that already exists the stock being added is zeroed
        // and the already existing stock get its stock count
        // therefore the sale value needs to be calculated before we
        // register the stock to the investor
        // THIS IS VERY IMPORTANT
        double saleValue = newStock.getTotalValue();
        this.investor.registerStock(newStock);
        try
        {
            this.investor.removeMoney(saleValue);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        this.company.addCapital(saleValue);
        this.investor.getAgent().isNotInvesting();
    }
}
