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
        if (moneyDelta > 0) {
            return;
        }
        if ((this.money - moneyDelta) < 0) {
            throw new Exception("Investor does not have enough money for that");
        }
        this.money -= moneyDelta;
    }

}
