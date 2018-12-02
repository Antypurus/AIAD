package DataExporter.EventLogger.Events;

public class ManagerInfoEvent extends Event
{

    private String manager_name;
    private String company_name;
    private double inteligence_factor;
    private double stupidity_factor;

    public ManagerInfoEvent(String manager_name, String company_name,
                            double inteligence_factor, double stupidity_factor)
    {
        this.manager_name = manager_name;
        this.company_name = company_name;
        this.inteligence_factor = inteligence_factor;
        this.stupidity_factor = stupidity_factor;
    }

    @Override
    public String get_csv_data()
    {
        return this.manager_name + "," + this.company_name + "," + this.inteligence_factor + "," + this.stupidity_factor;
    }

    @Override
    public String get_csv_header()
    {
        return "manager name,company name,intelligence factor,stupidity factor";
    }
}
