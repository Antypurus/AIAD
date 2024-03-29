package Company;

import Aggregators.Index;
import Common.Date;
import Company.Agents.CompanyAgent;
import Components.Product;
import Components.Stock;
import Components.StockValue;
import Components.Yield;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private CopyOnWriteArrayList<Manager> managers;
    private Date foundationDate;
    private MarketHistory history;
    private CompanyAgent agent;
    private Stock stock;
    private int innitialShareCount;

    private double capital;

    private CopyOnWriteArrayList<Product> products;

    public Company(String name, String acronym, Index index,
                   double qualityBias, Date foundationDate,
                   CompanyAgent agent, int shareCount, double capital)
    {
        this.qualityBias = qualityBias;
        this.name = name;
        this.acronym = acronym;
        this.index = index;
        this.subsidiaries = new ArrayList<>();
        this.yield = new Yield();
        this.managers = new CopyOnWriteArrayList<>();
        this.foundationDate = foundationDate;
        this.history = MarketHistory.generateMarketHistory(this);
        this.innitialShareCount = shareCount;
        this.stockValue =
                this.history.getStockValues().get(this.history.getStockValues().size() - 1);
        this.agent = agent;
        this.stock = new Stock(this, this.index, shareCount, null);
        this.capital = capital;
        this.products = new CopyOnWriteArrayList<>();
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
                   double capital, Yield yield)
    {
        this.qualityBias = qualityBias;
        this.name = name;
        this.acronym = acronym;
        this.index = index;
        this.subsidiaries = new ArrayList<>();
        this.yield = yield;
        this.managers = new CopyOnWriteArrayList<>();
        this.foundationDate = foundationDate;
        this.history = MarketHistory.generateMarketHistory(this);
        this.innitialShareCount = shareCount;
        this.stockValue =
                this.history.getStockValues().get(this.history.getStockValues().size() - 1);
        this.agent = agent;
        this.stock = new Stock(this, this.index, shareCount, null);
        this.capital = capital;
        this.products = new CopyOnWriteArrayList<>();
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

    public CopyOnWriteArrayList<Manager> getManagers()
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
        this.qualityBias = bias;
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

    public double getCapital()
    {
        return this.capital;
    }

    public void addCapital(double delta)
    {
        if (delta < 0)
        {
            return;
        }
        this.capital += delta;
    }

    public void reduceCapital(double delta)
    {
        if (delta < 0)
        {
            return;
        }
        this.capital -= delta;
    }

    public double getMonthDelta()
    {
        CopyOnWriteArrayList<StockValue> history = this.getMarketHistory().getStockValues();
        if (history.size() < 30)
        {
            return 0.0;
        }
        double now = history.get(history.size() - 1).getStockValue();
        double old = history.get(history.size() - 31).getStockValue();
        return now - old;
    }

    public synchronized void stockSync() throws Exception
    {
        if (this.getMarketHistory().getStockValueByDate(Date.CURRENT_DATE) == null)
        {
            this.getMarketHistory().addHistoricValue(Date.CURRENT_DATE,
                    this.getStockValue());
        }
    }

    public int getInnitialShareCount()
    {
        return this.innitialShareCount;
    }

    public void addProduct(Product product)
    {
        this.products.add(product);
    }

    public CopyOnWriteArrayList<Product> getProducts()
    {
        return this.products;
    }
}
