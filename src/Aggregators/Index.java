package Aggregators;

import Aggregators.Agents.IndexAgent;
import Company.Company;
import Components.Stock;
import Components.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Index
{
    /*<--------------FIELDS--------------------------->*/
    private String name;
    private ArrayList<Company> companies;
    private ArrayList<Stock> stocks;
    private HashMap<String, Stock> acronymToStocks; //maps company acronym to
    // all its stocks
    private HashMap<String, Company> companyAcronyms; //maps an acronym to its
    //company
    private HashMap<String, Company> companyNames; //maps an name to its company
    private ArrayList<Transaction> transactionRecords;

    private ConcurrentLinkedQueue<Transaction> transactionQueue;
    private ConcurrentLinkedQueue<Transaction> secondaryTransactionQueye;
    private boolean endOfDay = false;

    private IndexAgent agent = null;
    /*<--------------FIELDS--------------------------->*/


    /*<--------------METHODS--------------------------->*/
    public Index(String name)
    {
        this.name = name;
        this.companies = new ArrayList<Company>();
        this.stocks = new ArrayList<>();
        this.acronymToStocks = new HashMap<>();
        this.companyAcronyms = new HashMap<>();
        this.companyNames = new HashMap<>();
        this.transactionRecords = new ArrayList<>();
    }

    public String getName()
    {
        return this.name;
    }

    public void registerCompany(Company company) throws Exception
    {
        if (this.companyAcronyms.containsKey(company.getAcronym()))
        {
            throw new Exception("Company Acronym already registered");
        }
        if (this.companyNames.containsKey(company.getName()))
        {
            throw new Exception("Company name already registered");
        }
        this.companyNames.put(company.getName(), company);
        this.companyAcronyms.put(company.getAcronym(), company);
        this.companies.add(company);
        //might also want to register stocks for this company here
        //but for that need to get a way to have the company handle is
        //stock
    }

    public Company getCompanyByName(String name)
    {
        if (this.companyNames.containsKey(name))
        {
            return this.companyNames.get(name);
        }
        //log that there is no company
        return null;
    }

    public Company getCompanyByAcronym(String acronym)
    {
        if (this.companyAcronyms.containsKey(acronym))
        {
            return this.companyAcronyms.get(acronym);
        }
        //log that there is no company
        return null;
    }

    public ArrayList<Company> getAllCompanies()
    {
        return this.companies;
    }

    public void registerTranscation(Transaction transaction)
    {
        //TODO: validate transaction
        //TODO: execute transaction
        if (!this.endOfDay)
        {
            this.transactionQueue.add(transaction);
        } else
        {
            this.secondaryTransactionQueye.add(transaction);
        }
    }

    public void setAgent(IndexAgent agent)
    {
        this.agent = agent;
    }

    public IndexAgent getAgent()
    {
        return this.agent;
    }

    /*<--------------METHODS--------------------------->*/
}
