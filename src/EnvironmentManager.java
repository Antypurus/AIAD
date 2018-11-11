import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import Company.Company;
import Company.Manager;
import Components.Yield;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.Profile;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class EnvironmentManager
{
    //agents paths
    private static final String companyAgentPath = "Company.Agents" +
            ".CompanyAgent";
    private static final String dayTrackerPath = "Management" +
            ".DayTrackerAgent";
    private static final String indexPath = "Aggregators.Agents" + ".IndexAgent";
    private static final String investorPath = "Investor.Agents" + ".InvestorAgent";

    //jade components
    private Runtime runtime;
    private Profile profile;
    private ContainerController mainContainer;
    private ArrayList<AgentController> agentControllers;

    //investment components
    private Index index;
    private InvestorAgency agency;
    private ArrayList<Manager> managers;

    //control
    private boolean hasDayTracker = false;

    public EnvironmentManager(String filename) throws StaleProxyException, IOException
    {
        this.runtime = Runtime.instance();
        this.profile = new ProfileImpl();
        this.mainContainer = this.runtime.createMainContainer(this.profile);
        this.managers = new ArrayList<>();
        this.agentControllers = new ArrayList<>();

        this.loadEnvironment(filename);
        this.addDayTracker();
    }

    private void addCompany(String name, String acronym, double qualityBias,
                            Date foundationDate, int startingShares,
                            double StartingCapital) throws StaleProxyException
    {
        Object[] params = {name, acronym, this.index, qualityBias, foundationDate,
                startingShares, StartingCapital};
        AgentController agent = this.mainContainer.createNewAgent(name,
                companyAgentPath, params);
        agent.start();
        this.agentControllers.add(agent);
    }

    private void addIndex(String name) throws StaleProxyException
    {

        this.index = new Index(name);
        Object[] params = new Object[]{this.index};
        AgentController agent = this.mainContainer.createNewAgent(name,
                indexPath, params);
        agent.start();
        this.agentControllers.add(agent);
    }

    private void addCompany(String name, String acronym, double qualityBias,
                            Date foundationDate, int startingShares,
                            double StartingCapital, Yield yield) throws StaleProxyException
    {
        Object[] params = {name, acronym, this.index, qualityBias, foundationDate,
                startingShares, StartingCapital, yield};
        AgentController agent = this.mainContainer.createNewAgent(name,
                companyAgentPath, params);
        agent.start();
        this.agentControllers.add(agent);
    }

    private void addAgency(String name)
    {
        this.agency = new InvestorAgency(name);
    }

    private void addInvestor(String name, double riskBias, double startingMoney) throws StaleProxyException
    {
        Object[] params = {name, riskBias, this.agency, startingMoney, this.index};
        AgentController agent = this.mainContainer.createNewAgent(name,
                investorPath, params);
        agent.start();
        this.agentControllers.add(agent);
    }

    private void addDayTracker() throws StaleProxyException
    {
        if (!hasDayTracker)
        {
            Object[] params = {this.index, this.agency};
            AgentController agent = this.mainContainer.createNewAgent("Day " +
                    "Tracker", dayTrackerPath, params);
            agent.start();
            this.agentControllers.add(agent);
        }
    }

    private void addManager(String name, Company company,
                            double stupidityFactor, double intelligenceFactor,
                            double salary)
    {
        Manager manager = new Manager(name, company, stupidityFactor,
                intelligenceFactor, salary, 0.0);
        this.managers.add(manager);
    }

    private void loadEnvironment(String filename) throws IOException, StaleProxyException
    {
        URL path = EnvironmentManager.class.getResource(filename);
        File f = new File(path.getFile());
        BufferedReader reader = new BufferedReader(new FileReader(f));

        String line;
        while ((line = reader.readLine()) != null)
        {
            String[] args = line.split("<>");
            System.out.println(args[0]);

            String name;

            switch (args[0])
            {
                case "COMPANY":
                    name = args[1];
                    String acro = args[2];
                    double qualityBias = Double.valueOf(args[3]);

                    String[] date = args[4].split("/");
                    Date foundationDate = new Date(Integer.valueOf(date[0]),
                            Integer.valueOf(date[1]), Integer.valueOf(date[2]));

                    int startingShares = Integer.valueOf(args[5]);
                    double startingCapital = Double.valueOf(args[6]);

                    Yield yield = null;
                    if (args.length > 7)
                    {
                        yield = new Yield(Double.valueOf(args[7]));
                    }

                    if (yield == null)
                    {
                        this.addCompany(name, acro, qualityBias, foundationDate,
                                startingShares, startingCapital);
                    } else
                    {
                        this.addCompany(name, acro, qualityBias, foundationDate,
                                startingShares, startingCapital, yield);
                    }
                    break;

                case "INDEX":
                    name = args[1];
                    this.addIndex(name);
                    break;

                case "AGENCY":
                    name = args[1];
                    this.addAgency(name);
                    break;

                case "INVESTOR":
                    name = args[1];
                    double riskBias = Double.valueOf(args[2]);
                    double startingMoney = Double.valueOf(args[3]);
                    this.addInvestor(name, riskBias, startingMoney);
                    break;

                case "MANAGER":
                    name = args[1];
                    Company company = this.index.getCompanyByName(args[2]);
                    while(company==null)
                    {
                        company = this.index.getCompanyByName(args[2]);
                    }
                    double stupidity = Double.valueOf(args[3]);
                    double intelligence = Double.valueOf(args[4]);
                    double salary = Double.valueOf(args[5]);
                    this.addManager(name, company, stupidity, intelligence, salary);
                    break;

                default:
                    System.exit(1);
                    break;
            }
        }
    }

}
