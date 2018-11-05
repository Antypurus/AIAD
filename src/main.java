import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import Company.Company;
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
        Object[] params = new Object[2];
        params[0] = new String("Charlie");

        InvestorAgency agency = new InvestorAgency("NASDAQ");
        params[1] = agency;

        AgentController ac = cc.createNewAgent("Name","Investor.Agents" +
                ".InvestorAgent",params);
        ac.start();

        AgentController ac2 = cc.createNewAgent("Ding Dong","Investor.Agents" +
                        ".InvestorAgent",
                new Object[]{"Cuck McCuck",agency});
        ac2.start();

        Index index = new Index("NASDAQ");
        Company company = new Company("Tesla","TSLA",index,0.5001012432,
                new Date(1,1,
                2010));

    }
}