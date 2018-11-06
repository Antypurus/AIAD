package Components;

public class Yield
{

    private boolean hasYield = false;
    private double yield = 0.0;

    public Yield()
    {
    }

    public Yield(double yield)
    {
        this.hasYield = true;
        this.yield = yield;
    }

    public boolean hasYield()
    {
        return this.hasYield;
    }

    public double getYield()
    {
        return this.yield;
    }

    public void setYield(double yield)
    {
        if (yield > 0.0)
        {
            this.hasYield = true;
            this.yield = yield;
        } else
        {
            this.hasYield = false;
            this.yield = 0.0;
        }
    }

}
