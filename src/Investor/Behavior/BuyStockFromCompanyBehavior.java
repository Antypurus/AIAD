package Investor.Behavior;

import Common.Date;
import Company.Company;
import Investor.Investor;
import jade.core.behaviours.Behaviour;

public class BuyStockFromCompanyBehavior extends Behaviour
{
    private Investor investor;
    private Company company;
    private boolean done = false;

    public BuyStockFromCompanyBehavior(Investor investor, Company company)
    {
        this.investor = investor;
        this.company = company;
    }

    @Override
    public void action()
    {
        int quantityToBuy = this.calculateAmmountToBuy();
        System.out.println(Date.CURRENT_DATE+" :: "+this.investor.getName()+
                " has decided to buy "+ quantityToBuy+" stocks from "+this.company.getName());

        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }

    private int calculateAmmountToBuy()
    {
        double val =
                ((1-this.investor.getRiskBiasFactor())*((this.investor.getCurrentMoney()/this.company.getStock().getStockValue())*(Math.pow(Math.E,this.company.getYield().getYield())*Math.sqrt(company.getQualityBias()))));
        if((int)val>this.company.getStock().getShareCount())
        {
            val = this.company.getStock().getShareCount();
        }
        return (int)val;
    }
}
