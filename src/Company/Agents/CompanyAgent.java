package Company.Agents;

import Aggregators.Index;
import Common.Date;
import Company.Company;
import Components.Yield;
import jade.core.Agent;

public class CompanyAgent extends Agent {

    private Company company;

    public void setup() {
        Object[] args = this.getArguments();

        String name = args[0].toString();
        String acronym = args[1].toString();
        Index index = (Index) args[2];
        double qualityBias = (Double) args[3];
        Date foundationDate = (Date) args[4];
        Yield yield = null;
        if (args.length >= 6) {
            yield = (Yield) args[5];
        }

        if (yield == null) {
            this.company = new Company(name, acronym, index, qualityBias,
                    foundationDate, this);
        } else {
            this.company = new Company(name, acronym, index, qualityBias,
                    yield, foundationDate, this);
        }
    }

    public Company getCompany()
    {
        return this.company;
    }

}
