package Aggregators;

import Company.Company;
import Components.Stock;

import java.util.HashMap;

public class Index {

    private String name;
    private Company[] companies;
    private Stock[] stocks;
    private HashMap<String,Stock> acronymToStocks; //maps company acronym to
    // all its stocks
}
