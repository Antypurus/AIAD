package Company;

import Common.Date;

import java.util.Random;

public class Manager
{

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
        this.stupidity_factor = stupidity_factor;
        this.inteligence_factor = inteligence_factor;
        this.salary = salary;
        this.money = money;
        this.company.addManager(this);
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
        if (money < 0)
        {
            return;
        }
        this.money += money;
    }

    public void removeMoney(double money)
    {
        if (money < 0)
        {
            return;
        }
        this.money -= money;
    }

    public double getMoney()
    {
        return this.money;
    }

    public void randomAction()
    {
        Random generator = new Random();

        // Generate random number between 0 and 1
        double random = generator.nextDouble();
        // Generate random percentage between 10% and 50%
        double factor = (double) (generator.nextInt(50) + 10) / 100;
        // Get current company's quality Bias
        double qualityBias = this.company.getQualityBias();

        if (Math.abs(this.stupidity_factor - random) >= Math.abs(this.inteligence_factor - random))
        {
            // Do inteligent action
            this.company.setQualityBias(qualityBias * (1 + factor));
            System.out.println(Date.CURRENT_DATE+" :: "+this.name+" is a GOOD" +
                    " BOI");
        } else
        {
            // Do stupidy action
            this.company.setQualityBias(qualityBias * (1 - factor));
            System.out.println(Date.CURRENT_DATE+" :: "+this.name+" has done " +
                    "a stupid");
        }
    }
}
