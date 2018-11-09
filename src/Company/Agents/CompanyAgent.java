package Company.Agents;

import Aggregators.Index;
import Common.Date;
import Company.Behaviors.HandleBuyRequestBehavior;
import Company.Company;
import Components.Yield;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CompanyAgent extends Agent
{

    private Company company;

    public void setup()
    {
        Object[] args = this.getArguments();

        String name = args[0].toString();
        String acronym = args[1].toString();
        Index index = (Index) args[2];
        double qualityBias = (Double) args[3];
        Date foundationDate = (Date) args[4];
        Yield yield = null;
        int shareCount = (Integer)args[5];
        double  capital = (Double)args[6];
        if(args.length>=8)
        {
            yield = (Yield)args[7];
        }

        if (yield == null)
        {
            this.company = new Company(name, acronym, index, qualityBias,
                    foundationDate, this,shareCount,capital);
        } else
        {
            this.company = new Company(name, acronym, index, qualityBias,
                     foundationDate, this,shareCount,capital,yield);
        }

        this.addBehaviour(new HandleBuyRequestBehavior(this,
                new MessageTemplate(new MessageTemplate.MatchExpression()
                {
                    @Override
                    public boolean match(ACLMessage aclMessage)
                    {
                        return aclMessage.getPerformative() == ACLMessage.REQUEST;
                    }
                })));
    }

    public Company getCompany()
    {
        return this.company;
    }

}
