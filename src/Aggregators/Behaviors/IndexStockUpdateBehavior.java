package Aggregators.Behaviors;

import Aggregators.Index;
import Common.Date;
import Common.Pair;
import Company.Company;
import Components.Transaction;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IndexStockUpdateBehavior extends Behaviour
{

    private Index index;
    private boolean done = false;

    public IndexStockUpdateBehavior(Index index)
    {
        this.index = index;
    }

    @Override
    public void action()
    {
        this.index.endDay();

        System.out.println(this.index.getName()+" :: End of day :: "+ Date.CURRENT_DATE.getPreviousDay());

        ConcurrentLinkedQueue<Transaction> transactions =
                this.index.getPrimaryTransactionQueue();
        HashMap<Company, ArrayList<Pair<Double, Integer>>> transactionValues =
                new HashMap<>();

        for (Transaction transaction : transactions)
        {
            Company company = transaction.getStock().getCompany();
            if (transactionValues.containsKey(company))
            {
                transactionValues.get(company).add(new Pair<>(transaction.getShareValue(), transaction.getAmmount()));
            } else
            {
                ArrayList<Pair<Double, Integer>> values = new ArrayList<>();
                values.add(new Pair<>(transaction.getShareValue(),
                        transaction.getAmmount()));
                transactionValues.put(company, values);
            }
        }

        Set<Company> companies = transactionValues.keySet();
        for (Company company : companies)
        {
            ArrayList<Pair<Double, Integer>> values =
                    transactionValues.get(company);
            double comulativeShareValue = 0.0;
            int shareCount = 0;

            for(Pair<Double, Integer> shareValue:values)
            {
                comulativeShareValue+=(shareValue.left*shareValue.right);
                shareCount+=shareValue.right;
            }

            double newStockValue = comulativeShareValue/shareCount;
            company.getStockValue().setStockValue(newStockValue);
        }

        this.index.startDay();
        System.out.println(this.index.getName()+" :: Start of Day :: "+ Date.CURRENT_DATE);
        this.done = true;
    }

    @Override
    public boolean done()
    {
        return this.done;
    }
}
