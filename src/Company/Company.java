package Company;

import Aggregators.Index;
import Components.StockValue;
import Components.Yield;

import java.util.ArrayList;

public class Company {

    private String name;
    private String acronym;
    private Company owner = null;
    private boolean hasOwner = false;
    private ArrayList<Company>  subsidiaries;
    private Index index;
    private CompanyStatus status = CompanyStatus.OPERATIONAL;
    private StockValue stockValue;
    private Yield yield;


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

}
