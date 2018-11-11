package Company;

import Common.Date;
import Components.StockValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MarketHistory
{

    private static final double MAX_CHANGE = 0.01;

    private ConcurrentHashMap<Date, StockValue> history;
    private CopyOnWriteArrayList<Date> dates;
    private CopyOnWriteArrayList<StockValue> values;

    public MarketHistory()
    {
        this.history = new ConcurrentHashMap<>();
        this.dates = new CopyOnWriteArrayList<>();
        this.values = new CopyOnWriteArrayList<>();
    }

    public synchronized void addHistoricValue(Date date, StockValue value) throws Exception
    {
        if (this.dates.size() != 0)
        {
            if (Date.isSmaller(date, this.dates.get(this.dates.size() - 1)))
            {
                throw new Exception("Date is too old to be added now");
            } else
            {
                this.dates.add(date);
                this.values.add(value);
                this.history.put(date, value);
            }
        } else
        {
            this.dates.add(date);
            this.values.add(value);
            this.history.put(date, value);
        }
    }

    public static MarketHistory generateMarketHistory(Company company)
    {
        int daysToGenerate = Date.daysBetweenDates(company.getFoundationDate(),
                Date.CURRENT_DATE);
        double currentValues = 1;
        MarketHistory history = new MarketHistory();
        Date currentDate = company.getFoundationDate();
        try
        {
            history.addHistoricValue(currentDate,
                    new StockValue(currentValues));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        for (int i = 1; i <= daysToGenerate; ++i)
        {
            Random rand = new Random();
            double improveOrDecrease = rand.nextDouble();
            boolean improve = false;
            if (improveOrDecrease <= company.getQualityBias())
            {
                improve = true;
            }
            double delta = 0;
            if (improve)
            {
                delta =
                        1 + (MarketHistory.MAX_CHANGE) * rand.nextDouble();
            } else
            {
                delta =
                        1 - (MarketHistory.MAX_CHANGE) * rand.nextDouble();
            }
            currentValues = currentValues * delta;
            currentDate = currentDate.getNextDay();
            try
            {
                history.addHistoricValue(currentDate, new StockValue(currentValues));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return history;
    }

    @Override
    public String toString()
    {
        String ret = "";
        for (int i = 0; i < this.dates.size(); ++i)
        {
            ret += this.dates.get(i) + " : " + this.values.get(i).getStockValue() +
                    "\n";
        }
        return ret;
    }

    public synchronized CopyOnWriteArrayList<StockValue> getStockValues()
    {
        return this.values;
    }

    public synchronized CopyOnWriteArrayList<Date> getDates()
    {
        return this.dates;
    }

    public synchronized ConcurrentHashMap<Date, StockValue> getHistory()
    {
        return this.history;
    }

    public synchronized  StockValue getStockValueByDate(Date date)
    {
        if (this.history.containsKey(date))
        {
            return null;
        }
        return this.history.get(date);
    }
}
