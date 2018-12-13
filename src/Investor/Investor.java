package Investor;

import Aggregators.InvestorAgency;
import Common.Date;
import Company.Company;
import Components.Stock;
import Investor.Agents.InvestorAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Investor
{

    private String name;
    private double riskBiasFactor;
    private InvestorAgency agency;

    private volatile double money;
    private double reservedMoney = 0;

    private CopyOnWriteArrayList<Stock> portfolio;
    private ConcurrentHashMap<String, Stock> nameToStock;
    private ConcurrentHashMap<String, Stock> acronymToStock;

    private ConcurrentHashMap<Date, Double> capital_record;

    private InvestorAgent agent;

    public Investor(String name, double riskBiasFactor, InvestorAgency agency,
                    double startingMoney, InvestorAgent investorAgent)
    {
        this.name = name;
        this.money = startingMoney;
        this.riskBiasFactor = riskBiasFactor;
        this.agency = agency;
        this.portfolio = new CopyOnWriteArrayList<>();
        this.nameToStock = new ConcurrentHashMap<>();
        this.acronymToStock = new ConcurrentHashMap<>();
        this.agent = investorAgent;
        this.capital_record = new ConcurrentHashMap<>();
        try
        {
            this.agency.registerInvestor(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void register_capital(Date date,double capital)
    {
        this.capital_record.put(date,capital);
    }

    public void stockSync()
    {
        //check the portfolio
        for (Stock stock : portfolio)
        {
            if (stock.getShareCount() == 0)
            {
                this.portfolio.remove(stock);
                this.nameToStock.remove(stock.getCompany().getName());
                this.acronymToStock.remove(stock.getCompany().getAcronym());
            }
        }
    }

    public void registerStock(Stock stock)
    {
        if (this.acronymToStock.containsKey(stock.getCompany().getAcronym()))
        {
            this.acronymToStock.get(stock.getCompany().getAcronym()).increaseShareCount(stock.getShareCount());
            stock.setShareCount(0);
        } else
        {
            stock.setOwner(this);
            this.portfolio.add(stock);
            this.acronymToStock.put(stock.getCompany().getAcronym(), stock);
            this.nameToStock.put(stock.getCompany().getName(), stock);
            this.agency.registerStock(stock);
        }
    }

    public CopyOnWriteArrayList<Stock> getPortfolio()
    {
        return this.portfolio;
    }

    public Stock getStockByCompanyName(String name)
    {
        if (this.nameToStock.containsKey(name))
        {
            return this.nameToStock.get(name);
        }
        return null;
    }

    public Stock getStockByCompanyAcronym(String acronym)
    {
        if (this.acronymToStock.containsKey(acronym))
        {
            return this.acronymToStock.get(acronym);
        }
        return null;
    }

    public synchronized double getCurrentMoney()
    {
        return this.money;
    }

    public synchronized void setCurrentMoney(double money)
    {
        if (money < 0)
        {
            return;
        }
        this.money = money;
    }

    public synchronized void addMoney(double moneyDelta)
    {
        if (moneyDelta < 0)
        {
            return;
        }
        this.money += moneyDelta;
    }

    public synchronized void removeMoney(double moneyDelta) throws Exception
    {
        if (moneyDelta < 0)
        {
            return;
        }
        if ((this.money - moneyDelta) < 0)
        {
            //throw new Exception("Investor does not have enough money for " +
            //"that");
            return;
        }
        this.money -= moneyDelta;
    }

    public InvestorAgent getAgent()
    {
        return this.agent;
    }

    public void setAgent(InvestorAgent agent)
    {
        this.agent = agent;
    }

    public String getName()
    {
        return this.name;
    }

    public double getReservedMoney()
    {
        return this.reservedMoney;
    }

    public double getRiskBiasFactor()
    {
        return this.riskBiasFactor;
    }

    public boolean shouldBuy(Company company, double pricePerShare)
    {
        Random rand = new Random();
        double gamma = rand.nextDouble();

        double prob =
                (Math.pow(Math.E,this.riskBiasFactor)-1)*company.getQualityBias()*Math.sqrt(Math.abs(company.getStockValue().getStockValue()-pricePerShare+company.getMonthDelta()));
        if(pricePerShare>this.money)
        {
            prob = 0;
        }
        if(prob>1)
        {
            prob = 1;
        }

        return gamma <= prob;
    }

    public boolean shouldSell(Company company, double pricePerShare)
    {
        Random rand = new Random();
        double gamma = rand.nextDouble();

        double prob =
                (pricePerShare / company.getStockValue().getStockValue()) * (1 - this.riskBiasFactor) + Math.sqrt(company.getQualityBias() * company.getMonthDelta() - company.getYield().getYield());

        if (this.getCurrentMoney() < pricePerShare)
        {
            prob = 1;
        }

        boolean result = gamma <= prob;
        return result;
    }

    public double generateInitialOffer(Company company)
    {
        return company.getStockValue().getStockValue() * (1 - this.riskBiasFactor / 5);
    }

    public double generateCounter(Company company)
    {
        return company.getStockValue().getStockValue() * (1 + company.getQualityBias() / 5);
    }

    public InvestorAgency getAgency()
    {
        return this.agency;
    }

    public double getCapitalValue()
    {
        double val = this.money;
        for (Stock stock : this.portfolio)
        {
            val += stock.getTotalValue();
        }
        return val;
    }

    public double get_week_capital_delta()
    {
        if(this.capital_record.size()<7)
        {
            return 0.0f;
        }
        Date target_date = Date.CURRENT_DATE;
        for(int i=0;i<7;i++)
        {
            target_date = target_date.getPreviousDay();
        }
        return (this.getCapitalValue() - this.capital_record.get(target_date));
    }
}
