package Messages;

import Investor.Investor;
import jade.lang.acl.ACLMessage;

import java.io.Serializable;

public class RegisterAgentMessage implements Serializable {

    private Investor investor;

    public RegisterAgentMessage(Investor investor)
    {
        this.investor = investor;
    }

}
