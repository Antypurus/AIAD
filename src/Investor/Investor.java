package Investor;

import Aggregators.InvestorAgency;
import Company.Company;
import Components.Stock;
import Investor.Agents.InvestorAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Investor
{

    private String name;
    private double riskBiasFactor;
    private InvestorAgency agency;

    private double money;
    private double reservedMoney = 0;

    private ArrayList<Stock> portfolio;
    private HashMap<String, Stock> nameToStock;
    private HashMap<String, Stock> acronymToStock;

    private InvestorAgent agent;

    public Investor(String name, double riskBiasFactor, InvestorAgency agency,
                    double startingMoney, InvestorAgent investorAgent)
    {
        this.name = name;
        this.money = startingMoney;
        this.riskBiasFactor = riskBiasFactor;
        this.agency = agency;
        this.portfolio = new ArrayList<>();
        this.nameToStock = new HashMap<>();
        this.acronymToStock = new HashMap<>();
        this.agent = investorAgent;
        try
        {
            this.agency.registerInvestor(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void stockSync()
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

    public ArrayList<Stock> getPortfolio()
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
            throw new Exception("Investor does not have enough money for that");
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
        double gamma = ThreadLocalRandom.current().nextDouble(0, 1);

        double prob =
                (company.getMonthDelta() / company.getStockValue().getStockValue() + company.getYield().getYield()) * (1 - this.riskBiasFactor) * 2 * Math.sqrt(company.getQualityBias());

        double normalize =
                Math.abs(pricePerShare - company.getStockValue().getStockValue()) / company.getStockValue().getStockValue();

        if (normalize <= 0 || (pricePerShare - company.getStockValue().getStockValue()) < 0)
        {
            normalize = 1;
        }

        prob *= normalize;

        return gamma <= prob;
    }

    public boolean shouldSell(Company company, double pricePerShare)
    {
        double gamma = ThreadLocalRandom.current().nextDouble(0, 1);

        double prob =
                (pricePerShare/company.getStockValue().getStockValue())*(1-this.riskBiasFactor)+Math.sqrt(company.getQualityBias()*company.getMonthDelta()-company.getYield().getYield());

        if(this.getCurrentMoney()<pricePerShare)
        {
            prob=1;
        }

        boolean result = gamma<=prob;
        return result;
    }

    public double generateInitialOffer(Company company)
    {
        return company.getStockValue().getStockValue()*(1-this.riskBiasFactor/3);
    }

    public double generateCounter(Company company)
    {
        return company.getStockValue().getStockValue()*(1+company.getQualityBias()/4);
    }

    public  InvestorAgency getAgency()
    {
        return this.agency;
    }

}
