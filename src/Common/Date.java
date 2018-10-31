package Common;

public class Date {

    private int day;
    private int month;
    private int year;

    private static final int daysInMonth = 30;
    private static final int monthsInYear = 12;

    public Date(int day,int month,int year)
    {
        this.day=day;
        this.month=month;
        this.year=year;
    }

    public static int daysBetweenDates(Date date1, Date date2)
    {
        int end =
                date2.getYear()*monthsInYear*daysInMonth +
                        date2.getMonth()*daysInMonth + date2.getDay();
        int start =
                date1.getYear()*monthsInYear*daysInMonth +
                        date1.getMonth()*daysInMonth + date1.getDay();
        return (end-start);
    }

    public Date getNextDay()
    {
        int day = this.day;
        int month = this.month;
        int year = this.year;

        day++;
        if(day>daysInMonth)
        {
            day=1;
            month++;
        }
        if(month>monthsInYear)
        {
            month=1;
            year++;
        }

        return new Date(day,month,year);
    }

    public void incrementDay()
    {
        Date nextDay = this.getNextDay();
        this.day = nextDay.getDay();
        this.month = nextDay.getMonth();
        this.year = nextDay.getYear();
    }

    public Date getPreviousDay()
    {
        int day = this.day;
        int month = this.month;
        this.year = this.year;

        day--;
        if(day<=0)
        {
            day=daysInMonth;
            month--;
        }
        if(month<=0)
        {
            month=monthsInYear;
            year--;
        }

        return new Date(day,month,year);
    }

    public void decrementDay()
    {
        Date previousDay = this.getPreviousDay();
        this.day = previousDay.getDay();
        this.month = previousDay.getMonth();
        this.year = previousDay.getYear();
    }


    public int getDay()
    {
        return this.day;
    }

    public int getMonth()
    {
        return this.month;
    }

    public int getYear()
    {
        return this.year;
    }

}