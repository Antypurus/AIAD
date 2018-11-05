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

public class main {

    public static void main(String[] args) throws StaleProxyException {

        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();

        Index index = new Index("NASDAQ");

        ContainerController cc = rt.createMainContainer(p);

        InvestorAgency agency = new InvestorAgency("NASDAQ");

        String n1 = "Cuck McCuck";
        AgentController ac = cc.createNewAgent(n1, "Investor.Agents" +
                ".InvestorAgent", new Object[]{n1, 0.5, agency, 500.0, index});
        ac.start();

        String n2 = "Doris Beata";
        AgentController ac2 = cc.createNewAgent(n2, "Investor.Agents" +
                        ".InvestorAgent",
                new Object[]{n2, 0.5, agency, 500.0, index});
        ac2.start();

        AgentController ac4 = cc.createNewAgent("TESLA", "Company.Agents" +
                        ".CompanyAgent",
                new Object[]{"Tesla","TSLA",index,0.56,new Date(1,1,2010)});
        ac4.start();

        AgentController ac3 = cc.createNewAgent("Day Tracker", "Management" +
                        ".DayTrackerAgent",
                new Object[]{index});
        ac3.start();
    }
}