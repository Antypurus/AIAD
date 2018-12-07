package DataExporter.EventLogger.Events;

import Common.Date;

public class InvestorCapitalEvent extends Event
{

    private String investor_name;
    private double investor_risk;
    private double investor_capital;

    public InvestorCapitalEvent(String name, double risk, double capital)
    {
        this.investor_capital = capital;
        this.investor_name = name;
        this.investor_risk = risk;
    }

    @Override
    public String get_csv_data()
    {
        return this.investor_name + "," + this.investor_risk + "," + this.investor_capital;
    }

    @Override
    public String get_csv_header()
    {
        return "Name,Risk,Capital";
    }
}
