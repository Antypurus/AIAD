package Investor.Behavior;

import Aggregators.Index;
import Common.Date;
import Company.Company;
import Components.Stock;
import Components.Transaction;
import Investor.Investor;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuyStockFromAgentBehavior extends ContractNetInitiator
{

    volatile boolean accepted = false;

    private Investor investor;
    private CopyOnWriteArrayList<Investor> sources;
    private Company company;
    private int amount;
    private double initialOffer;

    private Index index;

    public BuyStockFromAgentBehavior(Investor investor,
                                     CopyOnWriteArrayList<Investor> sources,
                                     Company company)
    {
        super(investor.getAgent(), new ACLMessage(ACLMessage.CFP));

        this.investor = investor;
        this.sources = sources;
        this.company = company;
        this.amount = this.calculateAmountToBuy();

        if (this.sources.contains(this.investor))
        {
            this.sources.remove(this.investor);
        }

        System.out.println(Date.CURRENT_DATE + " :: " + this.investor.getName() +
                " has decided to buy " + this.amount + " stocks from " + this.company.getName());

        // this.listStockSources();
        this.index = this.investor.getAgent().getIndex();
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
        double offer = this.investor.generateInitialOffer(this.company);
        cfp = new ACLMessage(ACLMessage.CFP);
        int receiver_count = 0;

        for (Investor investor : this.sources)
        {
            String c_name = this.company.getName();
            Stock i_stock = investor.getStockByCompanyName(c_name);
            if (i_stock == null)
            {
                continue;
            }
            if(i_stock.getShareCount()<0)
            {
                continue;
            }
            if (i_stock.getShareCount() < this.amount)
            {
                if (investor.getStockByCompanyName(this.company.getName()).getShareCount() != 0)
                {
                    this.amount =
                            investor.getStockByCompanyName(this.company.getName()).getShareCount();
                }
            }
            receiver_count++;
            cfp.addReceiver(investor.getAgent().getAID());
        }

            this.initialOffer = offer;

            cfp.setContent("BUY::" + this.company.getAcronym() + "::" + this.amount + "::" + offer);
            cfp.setLanguage("BUY STOCK");

            messages.add(cfp);


        System.out.println(this.investor.getName() + "::BUY::" + this.company.getAcronym() +
                "::" + this.amount + "::" + offer + " sent to " + receiver_count +
                " potential receivers");
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
                if (msg.getPerformative() == ACLMessage.PROPOSE)
                {
                    if (msg.getLanguage().equals("ACCEPT PROPOSAL"))
                    {
                            ACLMessage resp = msg.createReply();
                            resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                            resp.setLanguage("ACCEPT");
                            resp.setContent(msg.getContent());
                            acceptances.add(resp);
                            System.out.println(this.investor.getName() + "::Sale " +
                                    "Confirmed at innitial offer");
                    }
                    if (msg.getLanguage().equals("COUNTER PROPOSAL"))
                    {
                        String[] args = msg.getContent().split("::");
                        double counter = Double.valueOf(args[1]);

                        boolean accept = this.investor.shouldBuy(this.company,
                                counter);

                        if (this.investor.getCurrentMoney() < counter * amount)
                        {
                            amount =
                                    (int) (this.investor.getCurrentMoney() / amount);
                        }

                        if (accept)
                        {

                                ACLMessage resp = msg.createReply();
                                resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                resp.setLanguage("ACCEPT");
                                resp.setContent("ACCEPT::" + counter);
                                acceptances.add(resp);
                                System.out.println(this.investor.getName() + "::Sale " +
                                        "Confirmed at counter offer");
                                accepted = true;

                        } else
                        {
                            double middle = ((counter + this.initialOffer) / 2);
                            accept = this.investor.shouldBuy(this.company, middle);

                            if (this.investor.getCurrentMoney() < amount * middle)
                            {
                                amount =
                                        (int) (this.investor.getCurrentMoney() / middle);
                            }

                            if (accept)
                            {

                                    ACLMessage resp = msg.createReply();
                                    resp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                    resp.setLanguage("COUNTER");
                                    resp.setContent("COUNTER::" + counter);
                                    acceptances.add(resp);
                                    System.out.println(this.investor.getName() + "::Sale " +
                                            "Final counter offer sent");
                                    accepted = true;

                            }
                        }
                    }
                    continue;

            }
            if(msg.getPerformative()==ACLMessage.ACCEPT_PROPOSAL)
            {
                ACLMessage resp = msg.createReply();
                resp.setPerformative(ACLMessage.REJECT_PROPOSAL);
                resp.setContent("REJECT");
                System.out.println(this.investor.getName() + "::Sale " +
                        "Sale Canceled");
                acceptances.add(resp);
            }
        }
    }

    @Override
    protected void handleAllResultNotifications(java.util.Vector resultNotifications)
    {
        for (int i = 0; i < resultNotifications.size(); ++i)
        {
            ACLMessage msg = (ACLMessage) resultNotifications.get(i);
            if (msg.getPerformative() == ACLMessage.CANCEL)
            {
                System.out.println(this.investor.getName() + "::Deal canceled\n");
                this.investor.getAgent().isNotInvesting();
            }
            if (msg.getPerformative() == ACLMessage.INFORM)
            {
                /*Execute Transaction*/

                String[] args = msg.getContent().split("::");
                double value = Double.valueOf(args[1]);
                String seller = args[2];
                Investor sellerInv =
                        this.investor.getAgency().getInvestorByName(seller);

                Transaction transaction =
                        new Transaction(sellerInv.getStockByCompanyName(company.getName()), sellerInv, this.investor, this.amount, value, Date.CURRENT_DATE);
                this.index.registerTransaction(transaction);

                /*Execute Transaction*/
                this.investor.getAgent().isNotInvesting();
                System.out.println(this.investor.getName() + "::Deal " +
                        "Completed with " + seller + " bought " + amount + " shares " +
                        "from " + company.getName() + " at $" + value + " each");
            }
        }
    }

    private int calculateAmountToBuy()
    {
        double val =
                (Math.pow(Math.E,
                        this.investor.getRiskBiasFactor() * (Math.abs(this.company.getMonthDelta() / this.company.getStockValue().getStockValue()) / this.investor.getCurrentMoney())));
        while (((new Double(val)).intValue() * this.company.getStockValue().getStockValue()) > this.investor.getCurrentMoney())
        {
            val = val / 2;
        }
        if ((int) val == 0)
        {
            if (this.investor.getCurrentMoney() == this.company.getStockValue().getStockValue())
            {
                val = 1;
            }
        }
        val = Math.abs(val);
        return (new Double(val)).intValue();
    }
}
