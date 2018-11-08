package Company.Behaviors;

import Company.Agents.CompanyAgent;
import Company.Company;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class HandleBuyRequestBehavior extends AchieveREResponder
{
    private CompanyAgent agent;

    public HandleBuyRequestBehavior(Agent a, MessageTemplate mt)
    {
        super(a, mt);

        if (!(a instanceof CompanyAgent))
        {
            System.out.println("FUCK OFF");
        }

        this.agent = (CompanyAgent) a;
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request)
    {
        String content = request.getContent();
        String[] args = content.split("::");
        int ammount = Integer.valueOf(args[2]);

        if (ammount <= this.agent.getCompany().getStock().getShareCount())
        {
            System.out.println(this.agent.getCompany().getName()+" :: Selling "+ammount+" stocks " +
                    "to "+args[0]);
            ACLMessage response = new ACLMessage(ACLMessage.AGREE);
            return response;
        } else
        {
            System.out.println(this.agent.getCompany().getName()+" :: " +
                    "Not Selling "+ammount+" stocks " +
                    "to "+args[0]);
            ACLMessage response = new ACLMessage(ACLMessage.REFUSE);
            return response;
        }
    }
}
