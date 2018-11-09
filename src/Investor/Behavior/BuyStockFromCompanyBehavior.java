package Investor.Behavior;

import Common.Date;
import Company.Company;
import Investor.Investor;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

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
        int quantityToBuy = this.calculateAmountToBuy();

        if(quantityToBuy*this.company.getStockValue().getStockValue()<=this.investor.getCurrentMoney())
        {
            System.out.println(Date.CURRENT_DATE + " :: " + this.investor.getName() +
                    " has decided to buy " + quantityToBuy + " stocks from " + this.company.getName());

            if (quantityToBuy != 0)
            {
                ACLMessage buyMessage = new ACLMessage(ACLMessage.REQUEST);
                buyMessage.setContent(this.investor.getName() + "::BUY::" + quantityToBuy);
                buyMessage.setEncoding("UTF-8");
                buyMessage.addReceiver(this.company.getAgent().getAID());

                BuyFromCompanyRequestBehavior requestBehavior =
                        new BuyFromCompanyRequestBehavior(this.investor.getAgent(), buyMessage);
                requestBehavior.target(this.investor, this.company, quantityToBuy);

                this.investor.getAgent().isCurrentlyInvesting();
                this.investor.getAgent().addBehaviour(requestBehavior);
            } else
            {
                System.out.println(Date.CURRENT_DATE + " :: Investor " + this.investor.getName() + " wil no be buying any stocks from " + this.company.getName() + " today");
            }
        }
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }

    private int calculateAmountToBuy()
    {
        double val =
                ((1 - this.investor.getRiskBiasFactor()) * ((this.investor.getCurrentMoney() / this.company.getStock().getStockValue()) * (Math.pow(Math.E, this.company.getYield().getYield()) * Math.sqrt(company.getQualityBias()))));
        if ((int) val > this.company.getStock().getShareCount())
        {
            val = this.company.getStock().getShareCount();
        }
        while (((int) val * this.company.getStockValue().getStockValue()) > this.investor.getCurrentMoney())
        {
            val = val / 2;
        }
        if ((int)val == 0)
        {
            if (this.investor.getCurrentMoney() >= this.company.getStockValue().getStockValue())
            {
                val = 1;
            }
        }
        return (int) val;
    }
}
