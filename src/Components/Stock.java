package Components;

import Aggregators.Index;
import Company.Company;
import Investor.Investor;

import java.util.concurrent.atomic.AtomicInteger;

public class Stock
{

    private Company company;
    private Index index;
    private AtomicInteger shareCount;
    private StockValue stockValue;
    private double totalValue;
    private Yield yield;
    private Investor owner;

    public Stock(Company company, Index index, int shareCount, Investor owner)
    {
        this.company = company;
        this.index = index;
        this.shareCount = new AtomicInteger(shareCount);
        this.owner = owner;
        this.stockValue = this.company.getStockValue();
        this.yield = this.company.getYield();
        this.getTotalValue();
    }

    public synchronized double getStockValue()
    {
        return this.stockValue.getStockValue();
    }

    public synchronized double getTotalValue()
    {
        this.totalValue = this.shareCount.get() * this.getStockValue();
        return this.totalValue;
    }

    public synchronized void setOwner(Investor owner)
    {
        this.owner = owner;
    }

    public Investor getOwner()
    {
        return this.owner;
    }

    public int getShareCount()
    {
        return this.shareCount.get();
    }

    public synchronized void setShareCount(int shareCount)
    {
        if (shareCount < 0)
        {
            return;
        }
        this.shareCount.set(shareCount);
    }

    public synchronized void increaseShareCount(int shareCountDelta)
    {
        if (shareCountDelta < 0)
        {
            return;
        }
        this.shareCount.weakCompareAndSetAcquire(this.shareCount.get(),
                this.shareCount.get()+shareCountDelta);
    }

    public synchronized void decreaseShareCount(int shareCountDelta)
    {
        if (shareCountDelta > 0)
        {
            return;
        }
        this.shareCount.weakCompareAndSetAcquire(this.shareCount.get(),
                this.shareCount.get()-shareCountDelta);
    }

    public Index getIndex()
    {
        return this.index;
    }

    public boolean hasYield()
    {
        return this.yield.hasYield();
    }

    public double getYield()
    {
        return this.yield.getYield();
    }

    public Company getCompany()
    {
        return this.company;
    }

    public synchronized Stock split(int shareAmount)
    {
        while(!this.shareCount.weakCompareAndSetAcquire(this.shareCount.get(),
                this.shareCount.get()-shareAmount));
        return new Stock(this.company,this.index,shareAmount,this.owner);
    }

}
