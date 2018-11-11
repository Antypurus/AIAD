package Investor.Behavior;

import Aggregators.Index;
import Company.Company;
import Components.Stock;
import Investor.Agents.InvestorAgent;
import Investor.Investor;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class HandleBuyRequestBehavior extends ContractNetResponder
{
    private Investor investor;
    private Index index;

    private String cache;

    private int amount;
    private Company company;

    public HandleBuyRequestBehavior(InvestorAgent a, Index index)
    {
        super(a, new MessageTemplate((MessageTemplate.MatchExpression) aclMessage ->
        {
            if (aclMessage.getContent() == null)
            {
                return false;
            }
            if (aclMessage.getLanguage() == null)
            {
                return false;
            }
            return aclMessage.getLanguage().equals("BUY STOCK");
        }));

        this.investor = a.getInvestor();
        this.index = index;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp)
    {
       // this.investor.getAgent().isCurrentlyInvesting();

        String[] args = cfp.getContent().split("::");
        Company company = this.index.getCompanyByAcronym(args[1]);
        int amount = Integer.valueOf(args[2]);
        double offer = Double.valueOf(args[3]);

        boolean sell = this.investor.shouldSell(company, offer);

        this.company = company;
        this.amount = amount;

        Stock stock = this.investor.getStockByCompanyAcronym(args[1]);
        if (stock == null)
        {
            System.out.println(this.investor.getName() + " :: I don't have " +
                    "stock of " + company.getName());
            return null;
        }
        if (stock.getShareCount() < amount)
        {
            System.out.println(this.investor.getName() + " :: I only have " + stock.getShareCount() +
                    " shares of" + company.getName());
            return null;
        }

        System.out.println(this.investor.getName()+":: RECEIVED MESSAGE:" + cfp.getContent());

        double counter = 0;
        if (!sell)
        {
            counter = this.investor.generateCounter(company);
            System.out.println(this.investor.getName() + " :: REFUSE " +
                    "Counter=" + counter);

            //respond with counter
            ACLMessage counterMsg = new ACLMessage(ACLMessage.PROPOSE);
            counterMsg.setLanguage("COUNTER PROPOSAL");
            counterMsg.setContent("COUNTER::" + counter);

            cache = "COUNTER::" + counter;

            return counterMsg;
        } else
        {
            System.out.println(this.investor.getName() + " :: ACCEPT " +
                    "Will sell at=" + offer);

            //respond with accept and sell
            ACLMessage acceptMessage = new ACLMessage(ACLMessage.PROPOSE);
            acceptMessage.setLanguage("ACCEPT PROPOSAL");
            acceptMessage.setContent("ACCEPT::" + offer);

            cache = "ACCEPT::" + offer;

            return acceptMessage;
        }
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
    {
        if (accept.getLanguage().equals("ACCEPT"))
        {
            String[] args = accept.getContent().split("::");
            double value = Double.valueOf(args[1]);
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setLanguage("ACCEPT");
            msg.setContent(accept.getContent()+"::"+this.investor.getName());

            this.investor.getAgent().isNotInvesting();

            return msg;
        }

        if (accept.getLanguage().equals("COUNTER"))
        {
            String[] args = accept.getContent().split("::");
            double counter = Double.valueOf(args[1]);

            boolean shouldAccept = this.investor.shouldSell(this.company,
                    counter);

            if (shouldAccept)
            {
                System.out.println("Accepting counter offer at " + counter);
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setLanguage("ACCEPT");
                msg.setContent("ACCEPT::" + counter+"::"+this.investor.getName());
                this.investor.getAgent().isNotInvesting();
                return msg;
            } else
            {
                System.out.println("Refuse to sell at counter of " + counter);
                this.investor.getAgent().isNotInvesting();
                return new ACLMessage(ACLMessage.FAILURE);
            }
        }
        this.investor.getAgent().isNotInvesting();
        return new ACLMessage(ACLMessage.FAILURE);
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
    {
        System.out.println("RECEIVED REJECT::" + accept.getContent());
    }
}
