package Components;

import Aggregators.Index;
import Company.Company;
import Investor.Investor;

public class Stock {

    private Company company;
    private Index index;
    private int shareCount;
    private StockValue stockValue;
    private double totalValue;
    private Yield yield;
    private double riskFactor;
    private Investor owner;

    public Stock(Company company, Index index, int shareCount, Investor owner)
    {
        this.company = company;
        this.index = index;
        this.shareCount = shareCount;
        this.owner = owner;
        this.stockValue = this.company.getStockValue();
        this.riskFactor = this.calculateRiskFactor();
        this.yield = this.company.getYield();
    }

    public double getStockValue()
    {
        return this.stockValue.getStockValue();
    }

    public double getTotalValue()
    {
        this.totalValue = this.shareCount * this.getStockValue();
        return this.totalValue;
    }

    public void setOwner(Investor owner)
    {
        this.owner = owner;
    }

    public Investor getOwner()
    {
        return this.owner;
    }

    public double getRiskFactor()
    {
        this.riskFactor = this.calculateRiskFactor();
        return this.riskFactor;
    }

    public int getShareCount()
    {
        return this.shareCount;
    }

    public void setShareCount(int shareCount)
    {
        if(shareCount<0)
        {
            return;
        }
        this.shareCount = shareCount;
    }

    public Index getIndex()
    {
        return this.index;
    }

    private double calculateRiskFactor()
    {
        return 0.0;
    }

    public boolean hasYield()
    {
        return this.yield.hasYield();
    }

    public double getYield()
    {
        return this.yield.getYield();
    }

}
