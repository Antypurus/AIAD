package Investor;

import Aggregators.InvestorAgency;
import Components.Stock;

import java.util.ArrayList;
import java.util.HashMap;

public class Investor {

    private String name;
    private double riskBiasFactor;
    private InvestorAgency agency;

    private double money;
    private ArrayList<Stock> portfolio;
    private HashMap<String, Stock> nameToStock;
    private HashMap<String, Stock> acronymToStock;

    public Investor(String name, double riskBiasFactor, InvestorAgency agency,
                    double startingMoney) {
        this.name = name;
        this.money = startingMoney;
        this.riskBiasFactor = riskBiasFactor;
        this.agency = agency;
        this.portfolio = new ArrayList<>();
        this.nameToStock = new HashMap<>();
        this.acronymToStock = new HashMap<>();
        try {
            this.agency.registerInvestor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stockSync() {
        //check the portfolio
        for (Stock stock : portfolio) {
            if (stock.getShareCount() == 0) {
                this.portfolio.remove(stock);
                this.nameToStock.remove(stock.getCompany().getName());
                this.acronymToStock.remove(stock.getCompany().getAcronym());
            }
        }
    }

    public void registerStock(Stock stock) {
        if (this.acronymToStock.containsKey(stock.getCompany().getAcronym())) {
            this.acronymToStock.get(stock.getCompany().getAcronym()).increaseShareCount(stock.getShareCount());
            stock.setShareCount(0);
        } else {
            stock.setOwner(this);
            this.portfolio.add(stock);
            this.acronymToStock.put(stock.getCompany().getAcronym(), stock);
            this.nameToStock.put(stock.getCompany().getName(), stock);
            this.agency.registerStock(stock);
            //register stock in the index
        }
    }

    public ArrayList<Stock> getPortfolio() {
        return this.portfolio;
    }

    public Stock getStockByCompanyName(String name) {
        if (this.nameToStock.containsKey(name)) {
            return this.nameToStock.get(name);
        }
        return null;
    }

    public Stock getStockByCompanyAcronym(String acronym) {
        if (this.acronymToStock.containsKey(acronym)) {
            return this.acronymToStock.get(acronym);
        }
        return null;
    }

    public double getCurrentMoney() {
        return this.money;
    }

    public void setCurrentMoney(double money) {
        if (money < 0) {
            return;
        }
        this.money = money;
    }

    public void addMoney(double moneyDelta) {
        if (moneyDelta < 0) {
            return;
        }
        this.money += moneyDelta;
    }

    public void removeMoney(double moneyDelta) throws Exception {
        if (moneyDelta < 0) {
            return;
        }
        if ((this.money - moneyDelta) < 0) {
            throw new Exception("Investor does not have enough money for that");
        }
        this.money -= moneyDelta;
    }

}
