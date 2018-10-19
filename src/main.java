import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class main
{
    public static void main(String[] args) throws StaleProxyException {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        ContainerController cc = rt.createAgentContainer(p);
        AgentController ac = cc.createNewAgent("Name","classe",new Object[1]);
        ac.start();
    }
}