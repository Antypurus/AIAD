package Aggregators;

import Company.Company;
import Components.Stock;
import Components.Transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class Index {

    private String name;
    private ArrayList<Company> companies;
    private ArrayList<Stock> stocks;
    private HashMap<String,Stock> acronymToStocks; //maps company acronym to
    // all its stocks
    private HashMap<String,Company> companyAcronyms; //maps an acronym to its
    //company
    private HashMap<String,Company> companyNames; //maps an name to its company
    private ArrayList<Transaction> transactionRecords;
}
