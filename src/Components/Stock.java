package Components;

import Aggregators.Index;
import Company.Company;
import Investor.Investor;

public class Stock {

    private Company company = null;
    private Index index = null;
    private int shareCount;
    private StockValue valuePerShader;
    private double totalValue;
    private boolean hasYield = false;
    private Double yield = null;
    private double riskFactor;
    private Investor owner;

    public Stock(Company company, Index index, int shareCount, Investor owner)
    {
        this.company = company;
        this.index = index;
        this.shareCount = shareCount;
        this.owner = owner;
        this.valuePerShader = this.company.getStockValue();
        this.riskFactor = this.calculateRikst();
    }

    public Stock(Company company, Index index, int shareCount, Investor owner
            , double yield)
    {
        this.company = company;
        this.index = index;
        this.shareCount = shareCount;
        this.owner = owner;
        this.valuePerShader = this.company.getStockValue();
        this.riskFactor = this.calculateRikst();
        this.hasYield = true;
        this.yield = yield;
    }

    private double calculateRikst()
    {
        return 0.0;
    }

    public boolean hasYield()
    {
        return this.hasYield;
    }

    public double getYield()
    {
        if(this.hasYield)
        {
            return this.yield;
        }
        return 0.0;
    }

}
