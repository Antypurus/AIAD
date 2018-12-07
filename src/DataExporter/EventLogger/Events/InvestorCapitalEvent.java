package DataExporter.EventLogger.Events;

import Common.Date;

public class InvestorCapitalEvent extends Event
{

    private String investor_name;
    private double investor_risk;
    private double investor_capital;
    private double week_delta;

    public InvestorCapitalEvent(String name, double risk, double capital,
                                double week_delta)
    {
        this.investor_capital = capital;
        this.investor_name = name;
        this.investor_risk = risk;
        this.week_delta = week_delta;
    }

    @Override
    public String get_csv_data()
    {
        return this.investor_name + "," + this.investor_risk + "," + this.investor_capital + "," + this.week_delta;
    }

    @Override
    public String get_csv_header()
    {
        return "Name,Risk,Capital,Week Delta";
    }
}
