package DataExporter.EventLogger.Events;

import Aggregators.Index;
import Aggregators.InvestorAgency;
import Common.Pair;
import Company.Company;
import Investor.Investor;

import java.util.concurrent.CopyOnWriteArrayList;

public class InvestorCapitalEvent extends Event
{

    private String investor_name;
    private double investor_risk;
    private double investor_capital;
    private double week_delta;

    private InvestorAgency agency;
    private Index index;

    public InvestorCapitalEvent(String name, double risk, double capital,
                                double week_delta, InvestorAgency agency,
                                Index index)
    {
        this.investor_capital = capital;
        this.investor_name = name;
        this.investor_risk = risk;
        this.week_delta = week_delta;

        this.agency = agency;
        this.index = index;
    }

    public Pair<Double, Integer> getAvgQualityCompanies()
    {
        final double avg_quality_lower_bound = 0.45;
        final double avg_quality_upper_bound = 0.55;

        CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();

        Integer avg_quality_count = 0;
        Double avg_quality_sum = 0.0;

        for (Company company : companies)
        {
            if (company.getQualityBias() >= avg_quality_lower_bound && company.getQualityBias() <= avg_quality_upper_bound)
            {
                avg_quality_count++;
                avg_quality_sum += company.getQualityBias();
            }
        }


        Double average_avg_quality_bias = 0.0;
        if (avg_quality_count > 0)
        {
            average_avg_quality_bias = avg_quality_sum / avg_quality_count;
        }

        return new Pair<Double, Integer>(average_avg_quality_bias, avg_quality_count);
    }

    public Pair<Double, Integer> getLowQualityCompanies()
    {
        final double low_quality_bound = 0.45;

        CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();

        Integer low_quality_count = 0;
        Double low_quality_sum = 0.0;

        for (Company company : companies)
        {
            if (company.getQualityBias() < low_quality_bound)
            {
                low_quality_count++;
                low_quality_sum += company.getQualityBias();
            }
        }

        Double average_low_quality_bias = 0.0;
        if (low_quality_count > 0)
        {
            average_low_quality_bias = low_quality_sum / low_quality_count;
        }

        return new Pair<Double, Integer>(average_low_quality_bias, low_quality_count);
    }

    public Pair<Double, Integer> getHighQualityCompanies()
    {
        final double High_quality_bound = 0.55;

        CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();

        Integer high_quality_count = 0;
        Double high_quality_sum = 0.0;

        for (Company company : companies)
        {
            if (company.getQualityBias() > High_quality_bound)
            {
                high_quality_count++;
                high_quality_sum += company.getQualityBias();
            }
        }

        Double average_high_quality_bias = 0.0;
        if (high_quality_count > 0)
        {
            average_high_quality_bias = high_quality_sum / high_quality_count;
        }

        return new Pair<Double, Integer>(average_high_quality_bias, high_quality_count);
    }

    public Pair<Double, Integer> getOverallCompanyQuality()
    {
        CopyOnWriteArrayList<Company> companies = this.index.getAllCompanies();
        Integer company_count = 0;
        Double company_quality = 0.0;

        for (Company company : companies)
        {
            company_count++;
            company_quality += company.getQualityBias();
        }

        return new Pair<Double, Integer>(company_quality / company_count,
                company_count);
    }

    public Pair<Double, Integer> getLowRiskAgents()
    {
        final double low_risk_bound = 0.45;

        CopyOnWriteArrayList<Investor> investors = this.agency.getInvestors();

        Integer low_risk_count = 0;
        Double low_risk_sum = 0.0;

        for (Investor investor : investors)
        {
            if (investor.getRiskBiasFactor() < low_risk_bound)
            {
                low_risk_count++;
                low_risk_sum += investor.getRiskBiasFactor();
            }
        }

        Double risk_average = 0.0;
        if (low_risk_count > 0)
        {
            risk_average = low_risk_sum / low_risk_count;
        }

        return new Pair<>(risk_average, low_risk_count);
    }

    public Pair<Double, Integer> getAverageRiskAgents()
    {
        final double average_risk_lower_bound = 0.45;
        final double average_risk_upper_bound = 0.55;

        Integer average_risk_count = 0;
        Double average_risk_sum = 0.0;

        CopyOnWriteArrayList<Investor> investors = this.agency.getInvestors();

        for (Investor investor : investors)
        {
            if (investor.getRiskBiasFactor() >= average_risk_lower_bound && investor.getRiskBiasFactor() <= average_risk_upper_bound)
            {
                average_risk_count++;
                average_risk_sum += investor.getRiskBiasFactor();
            }
        }

        Double risk_average = 0.0;
        if (average_risk_count > 0)
        {
            risk_average = average_risk_sum / average_risk_count;
        }

        return new Pair<>(risk_average, average_risk_count);
    }

    public Pair<Double, Integer> getHighRiskAgents()
    {
        final double high_risk_bound = 0.55;

        Integer high_risk_count = 0;
        Double high_risk_sum = 0.0;

        CopyOnWriteArrayList<Investor> investors = this.agency.getInvestors();

        for (Investor investor : investors)
        {
            if (investor.getRiskBiasFactor() > high_risk_bound)
            {
                high_risk_count++;
                high_risk_sum += investor.getRiskBiasFactor();
            }
        }

        Double risk_average = 0.0;
        if (high_risk_count > 0)
        {
            risk_average = high_risk_sum / high_risk_count;
        }

        return new Pair<>(risk_average, high_risk_count);
    }

    public Pair<Double, Integer> getOverallRisk()
    {
        Double riskSum = 0.0;
        CopyOnWriteArrayList<Investor> investors = this.agency.getInvestors();

        for (Investor investor : investors)
        {
            riskSum += investor.getRiskBiasFactor();
        }

        double average_riks = 0.0;
        if (investors.size() > 0)
        {
            average_riks = riskSum / investors.size();
        }

        return new Pair<>(average_riks, investors.size());
    }

    @Override
    public String get_csv_data()
    {
        Pair<Double, Integer> overall_c = this.getOverallCompanyQuality();
        Pair<Double, Integer> low_c = this.getLowQualityCompanies();
        Pair<Double, Integer> average_c = this.getAvgQualityCompanies();
        Pair<Double, Integer> high_c = this.getHighQualityCompanies();

        return this.investor_risk + "," + this.investor_capital
                + "," + this.week_delta
                + "," + overall_c.right + "," + overall_c.left
                + "," + low_c.right + "," + low_c.left
                + "," + average_c.right + "," + average_c.left
                + "," + high_c.right + "," + high_c.left
                ;
    }

    @Override
    public String get_csv_header()
    {
        return "Risk,Capital,Week Delta,Comapany Count,Number of Companies" +
                "Overall Average Comapany Quality,Low Quality Companies,Low " +
                "quality company " +
                "average," +
                "Average quality compnies,Average quality companies average," +
                "High quality companies,High quality company average";
    }
}
