package Company;

public class Manager {

    private String name;
    private Company company;
    private double stupidity_factor;
    private double inteligence_factor;
    private double salary;
    private double money;

    public Manager(String name, Company company, double stupidity_factor,
                   double inteligence_factor, double salary, double money)
    {
        this.name = name;
        this.company = company;
        this.company.addManager(this);
        this.stupidity_factor = stupidity_factor;
        this.inteligence_factor = inteligence_factor;
        this.salary = salary;
        this.money = money;
    }

    public String getName()
    {
        return this.name;
    }

    public double getSalary()
    {
        return this.salary;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public double getStupidityFactor()
    {
        return this.stupidity_factor;
    }

    public double getInteligenceFactor()
    {
        return this.inteligence_factor;
    }

    public void setInteligenceFactor(double inteligence_factor)
    {
        this.inteligence_factor = inteligence_factor;
    }

    public void setStupidityFactor(double stupidity_factor)
    {
        this.stupidity_factor = stupidity_factor;
    }

    public void addMoney(double money)
    {
        if(money<0)
        {
            return;
        }
        this.money+=money;
    }

    public void removeMoney(double money)
    {
        if(money<0)
        {
            return;
        }
        this.money -=money;
    }

    public double getMoney()
    {
        return this.money;
    }

}
