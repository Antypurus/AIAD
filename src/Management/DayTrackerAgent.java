package Management;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Date;
import Company.Behaviors.CompanyListStockValueBehavior;
import Company.Company;
import Investor.Investor;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DayTrackerAgent extends Agent
{

    public static final int TICK_RATE = 60;

    private Index index;
    private InvestorAgency agency;

    public void setup()
    {
        Object args[] = this.getArguments();
        this.index = (Index) args[0];
        this.agency = (InvestorAgency) args[1];

        this.addBehaviour(new TickingBehavior(this.index));
    }

    class TickingBehavior extends CyclicBehaviour
    {
        private Index index;
        private InvestorAgency agency;

        public TickingBehavior(Index index)
        {
            this.index = index;
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
            ArrayList<Company> companies = index.getAllCompanies();
            for (Company company : companies)
            {
                company.getAgent().addBehaviour(new CompanyListStockValueBehavior(company.getAgent()));
            }

            CopyOnWriteArrayList<Investor> investors = this.agency.getInvestors();
            for(Investor investor:investors)
            {
                investor.getAgent().newDayProtocol();
            }
        }
    }

}
