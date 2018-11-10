package Investor.Behavior;

import Aggregators.Index;
import Company.Company;
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
        super(a, new MessageTemplate(new MessageTemplate.MatchExpression()
        {
            @Override
            public boolean match(ACLMessage aclMessage)
            {
                if(aclMessage.getContent()==null)
                {
                    return false;
                }
                return aclMessage.getLanguage().equals("BUY STOCK");
            }
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

        System.out.println("RECEIVED MESSAGE:"+cfp.getContent());
        return null;
    }
}
