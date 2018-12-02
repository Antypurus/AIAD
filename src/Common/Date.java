package Common;

public class Date
{

    private int day;
    private int month;
    private int year;

    private static final int daysInMonth = 30;
    private static final int monthsInYear = 12;

    public static Date CURRENT_DATE = new Date(1, 1, 2018);

    public Date(int day, int month, int year)
    {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public static int daysBetweenDates(Date date1, Date date2)
    {
        int end =
                date2.getYear() * monthsInYear * daysInMonth +
                        date2.getMonth() * daysInMonth + date2.getDay();
        int start =
                date1.getYear() * monthsInYear * daysInMonth +
                        date1.getMonth() * daysInMonth + date1.getDay();
        return (end - start);
    }

    public Date getNextDay()
    {
        int day = this.day;
        int month = this.month;
        int year = this.year;

        day++;
        if (day > daysInMonth)
        {
            day = 1;
            month++;
        }
        if (month > monthsInYear)
        {
            month = 1;
            year++;
        }

        return new Date(day, month, year);
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
        int year = this.year;

        day--;
        if (day <= 0)
        {
            day = daysInMonth;
            month--;
        }
        if (month <= 0)
        {
            month = monthsInYear;
            year--;
        }

        return new Date(day, month, year);
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

    public static boolean isSmaller(Date date1, Date date2)
    {
        int nDate1 =
                date1.getDay() + date1.getMonth() * Date.daysInMonth + date1.getYear() * Date.monthsInYear * Date.daysInMonth;
        int nDate2 =
                date2.getDay() + date2.getMonth() * Date.daysInMonth + date2.getYear() * Date.monthsInYear * Date.daysInMonth;
        return nDate1 < nDate2;
    }

    @Override
    public String toString()
    {
        return "" + this.getDay() + "/" + this.getMonth() + "/" + this.getYear();
    }

    @Override
    public int hashCode()
    {
        return this.day+this.month*daysInMonth+this.year*daysInMonth*monthsInYear;
    }

    @Override
    public boolean equals(Object d2)
    {
        Date date2 = (Date) d2;
        if(this.day!=date2.day)
        {
            return false;
        }
        if(this.month!=date2.month)
        {
            return false;
        }
        if(this.year!=date2.year)
        {
            return false;
        }
        return true;
    }

}
