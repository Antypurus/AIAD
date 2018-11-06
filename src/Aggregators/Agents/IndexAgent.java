package Aggregators.Agents;

import Aggregators.Behaviors.IndexStockUpdateBehavior;
import Aggregators.Index;
import jade.core.Agent;

public class IndexAgent extends Agent
{

    private Index index;

    public void setup()
    {
        Object[] args = this.getArguments();

        this.index = (Index) args[0];
        this.index.setAgent(this);
    }

    public void newDayProtocol()
    {
        this.addBehaviour(new IndexStockUpdateBehavior(this.index));
    }

}
