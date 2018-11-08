package Company;

import Aggregators.Index;
import Common.Date;
import Company.Agents.CompanyAgent;
import Components.Stock;
import Components.StockValue;
import Components.Yield;

import java.util.ArrayList;

public class Company
{

    private String name;
    private String acronym;
    private Company owner = null;
    private boolean hasOwner = false;
    private ArrayList<Company> subsidiaries;
    private Index index;
    private CompanyStatus status = CompanyStatus.OPERATIONAL;
    private StockValue stockValue;
    private Yield yield;
    private double qualityBias;
    private ArrayList<Manager> managers;
    private Date foundationDate;
    private MarketHistory history;
    private CompanyAgent agent;
    private Stock stock;

    private double capital;

    public Company(String name, String acronym, Index index,
                   double qualityBias, Date foundationDate,
                   CompanyAgent agent, int shareCount,double capital)
    {
        this.qualityBias = qualityBias;
        this.name = name;
        this.acronym = acronym;
        this.index = index;
        this.subsidiaries = new ArrayList<>();
        this.yield = new Yield();
        this.managers = new ArrayList<>();
        this.foundationDate = foundationDate;
        this.history = MarketHistory.generateMarketHistory(this);
        this.stockValue =
                this.history.getStockValues().get(this.history.getStockValues().size() - 1);
        this.agent = agent;
        this.stock = new Stock(this, this.index, shareCount, null);
        this.capital = capital;
        try
        {
            this.index.registerCompany(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Company(String name, String acronym, Index index, double qualityBias,
                   Date foundationDate, CompanyAgent agent, int shareCount,
                   double capital,Yield yield)
    {
        this.qualityBias = qualityBias;
        this.name = name;
        this.acronym = acronym;
        this.index = index;
        this.subsidiaries = new ArrayList<>();
        this.yield = yield;
        this.managers = new ArrayList<>();
        this.foundationDate = foundationDate;
        this.history = MarketHistory.generateMarketHistory(this);
        this.stockValue =
                this.history.getStockValues().get(this.history.getStockValues().size() - 1);
        this.agent = agent;
        this.stock = new Stock(this, this.index, shareCount, null);
        this.capital = capital;
        try
        {
            this.index.registerCompany(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public StockValue getStockValue()
    {
        return this.stockValue;
    }

    public Yield getYield()
    {
        return this.yield;
    }

    public String getName()
    {
        return this.name;
    }

    public String getAcronym()
    {
        return this.acronym;
    }

    public ArrayList<Company> getSubsidiaries()
    {
        return this.subsidiaries;
    }

    public void addSubsidiary(Company subsidiary)
    {
        if (this.subsidiaries.contains(subsidiary))
        {
            return;
        }
        this.subsidiaries.add(subsidiary);
    }

    public void removeSubsidiary(Company subsidiary)
    {
        this.subsidiaries.remove(subsidiary);
    }

    public void setOwner(Company owner)
    {
        if (owner == null)
        {
            this.hasOwner = false;
            this.owner = null;
        } else
        {
            this.hasOwner = true;
            this.owner = owner;
        }
    }

    public CompanyStatus getCompanyStatus()
    {
        return this.status;
    }

    public void setCompanyStatus(CompanyStatus status)
    {
        this.status = status;
    }

    public void addManager(Manager manager)
    {
        if (this.managers.contains(manager))
        {
            return;
        }
        this.managers.add(manager);
    }

    public void removeManager(Manager manager)
    {
        this.managers.remove(manager);
    }

    public ArrayList<Manager> getManagers()
    {
        return this.managers;
    }

    public double getQualityBias()
    {
        return this.qualityBias;
    }

    public void setQualityBias(double bias)
    {
        if (bias < 0 || bias > 1)
        {
            return;
        }
        this.qualityBias = qualityBias;
    }

    public Date getFoundationDate()
    {
        return this.foundationDate;
    }

    public MarketHistory getMarketHistory()
    {
        return this.history;
    }

    public CompanyAgent getAgent()
    {
        return agent;
    }

    public Index getIndex()
    {
        return this.index;
    }

    public Stock getStock()
    {
        return this.stock;
    }
}
