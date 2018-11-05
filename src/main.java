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

        ContainerController cc = rt.createMainContainer(p);

        InvestorAgency agency = new InvestorAgency("NASDAQ");

        String n1 = "Cuck McCuck";
        AgentController ac = cc.createNewAgent(n1, "Investor.Agents" +
                ".InvestorAgent", new Object[]{n1, 0.5, agency, 500.0});
        ac.start();

        String n2 = "Doris Beata";
        AgentController ac2 = cc.createNewAgent(n2, "Investor.Agents" +
                        ".InvestorAgent",
                new Object[]{n2, 0.5, agency, 500.0});
        ac2.start();

        AgentController ac3 = cc.createNewAgent("Day Tracker", "Management" +
                        ".DayTrackerAgent",
                new Object[]{});
        ac3.start();

        Index index = new Index("NASDAQ");
        Company company = new Company("Tesla", "TSLA", index, 0.5001012432,
                new Date(1, 1,
                        2010));

    }
}