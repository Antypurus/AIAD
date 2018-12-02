package DataExporter.EventLogger.Events;

public class CompanyCapitalEvent extends Event
{

    private double month_delta;
    private String company_name;
    private double stock_value;
    private double quality_bias;

    public CompanyCapitalEvent(String name,double stock_value,
                               double quality_bias,double month_delta)
    {
        this.month_delta = month_delta;
        this.company_name = name;
        this.stock_value = stock_value;
        this.quality_bias = quality_bias;
    }

    @Override
    public String get_csv_data()
    {
        return this.company_name+","+this.stock_value+","+this.quality_bias+
                ","+this.month_delta;
    }

    @Override
    public String get_csv_header()
    {
        return "company name,stock value,quality bias,month delta";
    }
}
