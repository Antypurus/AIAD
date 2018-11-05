package Management;

import Common.Date;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class DayTrackerAgent extends Agent {

    public static final int TICK_RATE = 60;

    public void setup() {
        this.addBehaviour(new TickingBehavior());
    }

    class TickingBehavior extends CyclicBehaviour
    {

        @Override
        public void action() {
            Date.CURRENT_DATE.incrementDay();
            // do any updates that need to be done
            try {
                Thread.sleep((int)((1.0/DayTrackerAgent.TICK_RATE)*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
