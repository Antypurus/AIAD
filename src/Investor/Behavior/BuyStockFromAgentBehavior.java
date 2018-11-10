package Investor.Behavior;

import Common.Date;
import Company.Company;
import Investor.Investor;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuyStockFromAgentBehavior extends ContractNetInitiator
{

    private Investor investor;
    private CopyOnWriteArrayList<Investor> sources;
    private Company company;
    private int ammount;

    public BuyStockFromAgentBehavior(Investor investor,
                                     CopyOnWriteArrayList<Investor> sources,
                                     Company company)
    {
        super(investor.getAgent(), new ACLMessage(ACLMessage.CFP));

        this.investor = investor;
        this.sources = sources;
        this.company = company;
        this.ammount = this.calculateAmountToBuy();

        if (this.sources.contains(this.investor))
        {
            this.sources.remove(this.investor);
        }

        System.out.println(Date.CURRENT_DATE + " :: " + this.investor.getName() +
                " has decided to buy " + this.ammount + " stocks from " + this.company.getName());

        this.listStockSources();
        ;
    }

    private void listStockSources()
    {
        for (Investor investor : this.sources)
        {
            System.out.println(this.investor.getName() + " :: " + Date.CURRENT_DATE + " :: " +
                    "Stock Source Option " + investor.getName());
        }
    }

    @Override
    protected java.util.Vector prepareCfps(ACLMessage cfp)
    {
        Vector<ACLMessage> messages = new Vector<>();

        cfp = new ACLMessage(ACLMessage.CFP);

        for (Investor investor : this.sources)
        {
            cfp.addReceiver(investor.getAgent().getAID());
        }
        double offfer = this.investor.generateInitialOffer(this.company);
        cfp.setContent("BUY::" + this.company.getAcronym() + "::" + this.ammount + "::" + offfer);
        cfp.setLanguage("BUY STOCK");

        messages.add(cfp);

        return messages;
    }


    @Override
    protected void handleAllResponses(java.util.Vector responses,
                                      java.util.Vector acceptances)
    {
        for (int i = 0; i < responses.size(); ++i)
        {
            ACLMessage msg = (ACLMessage) responses.get(i);
            System.out.println(msg.getContent());

            ACLMessage resp = msg.createReply();
            resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            resp.setContent("TEST");
            acceptances.add(resp);
        }
    }

    private int calculateAmountToBuy()
    {
        double val =
                ((1 - this.investor.getRiskBiasFactor()) * ((this.investor.getCurrentMoney() / this.company.getStock().getStockValue()) * (Math.pow(Math.E, this.company.getYield().getYield()) * Math.sqrt(company.getQualityBias()))));
        if ((int) val > this.company.getStock().getShareCount())
        {
            val = this.company.getStock().getShareCount();
        }
        while (((int) val * this.company.getStockValue().getStockValue()) > this.investor.getCurrentMoney())
        {
            val = val / 2;
        }
        if ((int) val == 0)
        {
            if (this.investor.getCurrentMoney() >= this.company.getStockValue().getStockValue())
            {
                val = 1;
            }
        }
        return (int) val;
    }
}
