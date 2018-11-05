package Aggregators;

import Company.Company;
import Components.Stock;
import Investor.Investor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InvestorAgency {

    private String name;
    private CopyOnWriteArrayList<Investor> investors;
    private ConcurrentHashMap<Company, ArrayList<Investor>> investorWithStock;

    public InvestorAgency(String name) {
        this.name = name;
        this.investors = new CopyOnWriteArrayList<Investor>();
        this.investorWithStock = new ConcurrentHashMap<Company, ArrayList<Investor>>();
    }

    public void registerInvestor(Investor investor) throws Exception {
        if (investors.contains(investor)) {
            throw new Exception("Investor already registered here");
        }
        this.investors.add(investor);
        for (Stock stock : investor.getPortfolio()) {
            if (stock.getShareCount() != 0) {
                if (this.investorWithStock.containsKey(stock.getCompany())) {
                    this.investorWithStock.get(stock.getCompany()).add(investor);
                } else {
                    ArrayList<Investor> companyInvestors =
                            new ArrayList<Investor>();
                    companyInvestors.add(investor);
                    this.investorWithStock.put(stock.getCompany(), companyInvestors);
                }
            }
        }
    }

    public void registerStock(Stock stock) {
        if (this.investorWithStock.containsKey(stock.getCompany())) {
            if (this.investorWithStock.get(stock.getCompany()).contains(stock.getOwner())) {
                return;
            } else {
                this.investorWithStock.get(stock.getCompany()).add(stock.getOwner());
            }
        } else {
            ArrayList<Investor> companyInvestors = new ArrayList<>();
            companyInvestors.add(stock.getOwner());
            this.investorWithStock.put(stock.getCompany(), companyInvestors);
        }
    }

    public CopyOnWriteArrayList<Investor> getInvestors()
    {
        return this.investors;
    }

}
