package Investor.Behavior;

import Aggregators.Index;
import Investor.Agents.InvestorAgent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SSResponderDispatcher;

public class AgentReceiverDispatcher extends SSResponderDispatcher
{
    private InvestorAgent agent = null;
    private Index index = null;

    public AgentReceiverDispatcher(InvestorAgent agent,Index index)
    {
        super(agent, new MessageTemplate((MessageTemplate.MatchExpression) aclMessage ->
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

        this.agent = agent;
        this.index = index;
    }

    @Override
    protected Behaviour createResponder(ACLMessage aclMessage)
    {
        return new HandleBuyRequestBehavior(this.agent,this.index,aclMessage);
    }
}
