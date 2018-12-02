import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import Company.Company;
import DataExporter.Exporter.DataExporter;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import Company.Manager;

import java.io.IOException;

public class main
{

    public static void main(String[] args) throws StaleProxyException
    {

        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        //p.setParameter(Profile.GUI,"true");


        ContainerController cc = rt.createMainContainer(p);

        try {
            EnvironmentManager environmentManager = new EnvironmentManager("agents.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(new DataExporter());
        thread.start();

        try
        {
            thread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}