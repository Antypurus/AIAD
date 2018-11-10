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
    private double innitialOffer;

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
        this.investor.getAgent().isCurrentlyInvesting();
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
        this.innitialOffer = offfer;

        cfp.setContent("BUY::" + this.company.getAcronym() + "::" + this.ammount + "::" + offfer);
        cfp.setLanguage("BUY STOCK");

        messages.add(cfp);

        return messages;
    }


    @Override
    protected void handleAllResponses(java.util.Vector responses,
                                      java.util.Vector acceptances)
    {
        boolean accepted = false;
        for (int i = 0; i < responses.size(); ++i)
        {
            ACLMessage msg = (ACLMessage) responses.get(i);
            if (!accepted)
            {
                if (msg.getLanguage().equals("ACCEPT PROPOSAL"))
                {
                    ACLMessage resp = msg.createReply();
                    resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    resp.setLanguage("ACCEPT");
                    resp.setContent(msg.getContent());
                    acceptances.add(resp);
                }
                if (msg.getLanguage().equals("COUNTER PROPOSAL"))
                {
                    String[] args = msg.getContent().split("::");
                    double counter = Double.valueOf(args[1]);

                    boolean accept = this.investor.shouldBuy(this.company,
                            counter);

                    if (this.investor.getCurrentMoney() < counter * ammount)
                    {
                        accept = false;
                    }

                    if (accept)
                    {
                        ACLMessage resp = msg.createReply();
                        resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        resp.setLanguage("ACCEPT");
                        resp.setContent("ACCEPT::" + counter);
                        acceptances.add(resp);
                    } else
                    {
                        double middle = ((counter + this.innitialOffer) / 2);
                        accept = this.investor.shouldBuy(this.company, middle);

                        if (this.investor.getCurrentMoney() < ammount * middle)
                        {
                            accept = false;
                        }

                        if (accept)
                        {
                            ACLMessage resp = msg.createReply();
                            resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                            resp.setLanguage("COUNTER");
                            resp.setContent("COUNTER::" + counter);
                            acceptances.add(resp);
                        }
                    }
                }
            }
            ACLMessage resp = msg.createReply();
            resp.setPerformative(ACLMessage.REJECT_PROPOSAL);
            resp.setContent("REJECT");
            acceptances.add(resp);
        }
        if (!accepted)
        {
            this.investor.getAgent().isNotInvesting();
        }
    }

    @Override
    protected void handleAllResultNotifications(java.util.Vector resultNotifications)
    {
        for (int i = 0; i < resultNotifications.size(); ++i)
        {
            ACLMessage msg = (ACLMessage) resultNotifications.get(i);
            if (msg.getPerformative() == ACLMessage.FAILURE)
            {
                System.out.println(this.investor.getName() + "::Deal canceled\n");
                this.investor.getAgent().isNotInvesting();
            }
            if (msg.getPerformative() == ACLMessage.INFORM)
            {
                System.out.println(this.investor.getName() + "::Deal " +
                        "Completed\n");
                /*Execute Transaction*/

                String[] args = msg.getContent().split("::");
                double value = Double.valueOf(args[1]);

                this.investor.getStockByCompanyName(this.company.getName()).increaseShareCount(this.ammount);
                try
                {
                    this.investor.removeMoney(this.ammount*value);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                /*Execute Transaction*/
                this.investor.getAgent().isNotInvesting();
            }
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
