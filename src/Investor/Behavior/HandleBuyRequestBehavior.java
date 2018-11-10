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

    public HandleBuyRequestBehavior(InvestorAgent a, Index index)
    {
        super(a, new MessageTemplate((MessageTemplate.MatchExpression) aclMessage ->
        {
            if (aclMessage.getContent() == null)
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
        String[] args = cfp.getContent().split("::");
        Company company = this.index.getCompanyByAcronym(args[1]);
        int ammount = Integer.valueOf(args[2]);
        double offer = Double.valueOf(args[3]);

        boolean sell = this.investor.shouldSell(company, offer);

        Stock stock = this.investor.getStockByCompanyAcronym(args[1]);
        if (stock == null)
        {
            System.out.println(this.investor.getName() + " :: I dont have " +
                    "stock of " + company.getName());
            return null;
        }
        if (stock.getShareCount() < ammount)
        {
            System.out.println(this.investor.getName() + " :: I only have " + stock.getShareCount() +
                    " shares of" + company.getName());
            return null;
        }

        System.out.println("RECEIVED MESSAGE:" + cfp.getContent());

        double counter = 0;
        if (!sell)
        {
            counter = this.investor.generateCounter(company);
            System.out.println(this.investor.getName() + " :: REFUSE " +
                    "Coutner=" + counter);

            //respond with counter
            ACLMessage counterMsg = new ACLMessage(ACLMessage.PROPOSE);
            counterMsg.setLanguage("COUNTER PROPOSAL");
            counterMsg.setContent("COUNTER::"+counter);
            return  counterMsg;
        } else
        {
            System.out.println(this.investor.getName() + " :: ACCEPT " +
                    "Will sell at=" + offer);

            //respond with accept and sell
            ACLMessage acceptMessage = new ACLMessage(ACLMessage.CFP);
            acceptMessage.setLanguage("ACCEPT PROPOSAL");
            acceptMessage.setContent("ACCEPT::"+offer);
            return acceptMessage;
        }
    }
}
