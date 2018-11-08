package Company.Agents;

import Aggregators.Index;
import Common.Date;
import Company.Company;
import Components.Yield;
import jade.core.Agent;

public class CompanyAgent extends Agent
{

    private Company company;

    public void setup()
    {
        Object[] args = this.getArguments();

        String name = args[0].toString();
        String acronym = args[1].toString();
        Index index = (Index) args[2];
        double qualityBias = (Double) args[3];
        Date foundationDate = (Date) args[4];
        Yield yield = null;
        int shareCount;
        if (args.length >= 7)
        {
            yield = (Yield) args[5];
            shareCount = (Integer) args[6];
        }
        else
        {
            shareCount = (Integer)args[5];
        }

        if (yield == null)
        {
            this.company = new Company(name, acronym, index, qualityBias,
                    foundationDate, this,shareCount);
        } else
        {
            this.company = new Company(name, acronym, index, qualityBias,
                    yield, foundationDate, this,shareCount);
        }
    }

    public Company getCompany()
    {
        return this.company;
    }

}
