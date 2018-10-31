import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class main
{

    public static void main(String[] args) throws StaleProxyException {

        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        ContainerController cc = rt.createMainContainer(p);
        Object[] params = new Object[1];
        params[0] = new String("Charlie");
        AgentController ac = cc.createNewAgent("Name","Investor.Investor",params);
        ac.start();

        AgentController ac2 = cc.createNewAgent("Ding Dong","Investor.Investor",new Object[]{"Cuck McCuck"});
        ac2.start();

    }
}