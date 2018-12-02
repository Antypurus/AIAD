package Management;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import Company.Behaviors.CheckForBankrupcyBehavior;
import Company.Behaviors.CheckSalesBehavior;
import Company.Behaviors.CompanyListStockValueBehavior;
import Company.Behaviors.ProductLaunchBehavior;
import Company.Company;
import DataExporter.EventLogger.EventLogger;
import DataExporter.EventLogger.Events.CompanyCapitalEvent;
import Investor.Investor;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import Company.Manager;

import java.util.concurrent.CopyOnWriteArrayList;

public class DayTrackerAgent extends Agent
{

    public static final int TICK_RATE = 1;

    private Index index;
    private InvestorAgency agency;

    public void setup()
    {
        Object args[] = this.getArguments();
        this.index = (Index) args[0];
        this.agency = (InvestorAgency) args[1];

        System.out.println("---------Started Daily Processing-------");

        this.addBehaviour(new TickingBehavior(this.index, this.agency));
    }

    class TickingBehavior extends CyclicBehaviour
    {
        private Index index;
        private InvestorAgency agency;

        public TickingBehavior(Index index, InvestorAgency agency)
        {
            this.index = index;
            this.agency = agency;
        }

        @Override
        public void action()
        {
            Date.CURRENT_DATE.incrementDay();
            this.newDayProtocol();
            try
            {
                Thread.sleep((int) ((1.0 / DayTrackerAgent.TICK_RATE) * 1000));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        private void newDayProtocol()
        {
            this.index.getAgent().newDayProtocol();
            CopyOnWriteArrayList<Company> companies = index.getAllCompanies();
            for (Company company : companies)
            {
                EventLogger.register_event(new CompanyCapitalEvent(company.getName(),company.getStockValue().getStockValue(),company.getQualityBias(),company.getMonthDelta()),Date.CURRENT_DATE, company.getAgent().slot);

                company.getAgent().addBehaviour(new CompanyListStockValueBehavior(company.getAgent()));

                CopyOnWriteArrayList<Manager> managers = company.getManagers();
                for (Manager manager : managers)
                {
                    manager.randomAction();
                }

                try
                {
                    company.stockSync();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                company.getAgent().addBehaviour(new CheckForBankrupcyBehavior(company));
                company.getAgent().addBehaviour(new ProductLaunchBehavior(company));
                company.getAgent().addBehaviour(new CheckSalesBehavior(company));
            }

            CopyOnWriteArrayList<Investor> investors = this.agency.getInvestors();
            for (Investor investor : investors)
            {
                investor.getAgent().newDayProtocol();
            }

        }
    }

}
