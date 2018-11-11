package Company.Behaviors;

import Company.Company;
import Company.Manager;
import Company.CompanyStatus;
import jade.core.behaviours.Behaviour;

public class CheckForBankrupcyBehavior extends Behaviour
{
    private Company company;
    private boolean done = false;

    public CheckForBankrupcyBehavior(Company company)
    {
        this.company = company;
    }

    @Override
    public void action()
    {
        if(this.company.getCapital()<=0)
        {
            System.out.println(this.company.getName()+"HAS GONE BANKRUPT");
            this.company.setCompanyStatus(CompanyStatus.BANKRUPT);
            this.company.getStockValue().setStockValue(0.0);
            this.company.getAgent().doDelete();
        }

        double cumulativeSalary = 0.0;

        for (Manager manager : this.company.getManagers())
        {
            cumulativeSalary += manager.getSalary();
        }

        if(cumulativeSalary>=this.company.getCapital())
        {
            System.out.println(this.company.getName()+"HAS GONE BANKRUPT");
            this.company.setCompanyStatus(CompanyStatus.BANKRUPT);
            this.company.getStockValue().setStockValue(0.0);
            this.company.getAgent().doDelete();
        }

        double cummulativeYieldPayout =
                company.getInnitialShareCount()*company.getStockValue().getStockValue()*company.getYield().getYield();
        if(cummulativeYieldPayout>=this.company.getCapital())
        {
            System.out.println(this.company.getName()+"HAS GONE BANKRUPT");
            this.company.setCompanyStatus(CompanyStatus.BANKRUPT);
            this.company.getStockValue().setStockValue(0.0);
            this.company.getAgent().doDelete();
        }


        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
