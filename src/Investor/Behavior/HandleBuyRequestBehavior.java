package Investor.Behavior;

import Investor.Agents.InvestorAgent;
import Investor.Investor;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class HandleBuyRequestBehavior extends ContractNetResponder
{
    private Investor investor;

    public HandleBuyRequestBehavior(InvestorAgent a)
    {
        super(a, new MessageTemplate((MessageTemplate.MatchExpression) aclMessage -> aclMessage.getOntology().equals("BUY STOCK")));

        this.investor = a.getInvestor();
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp)
    {
        return null;
    }
}
