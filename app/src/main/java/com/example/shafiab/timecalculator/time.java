package com.example.shafiab.timecalculator;

/**
 * Created by shafiab on 9/3/14.
 */
public class time {
    double hour;
    double min;
    double sec;
    boolean isMinus;

    time()
    {
        isMinus = false;
        hour = 0;
        min = 0;
        sec = 0;
    }


    time(double []t, boolean isMinus)
    {

        this.isMinus = isMinus;
        this.hour = t[0];
        this.min = t[1];
        this.sec = t[2];
    }


    public void setTime(time A)
    {
        this.hour = A.hour;
        this.min = A.min;
        this.sec = A.sec;
        this.isMinus = A.isMinus;
    }


    public void setTime(double hour, double min, double sec, boolean isMinus)
    {
        this.isMinus = isMinus;

        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public double toSec()
    {
        if (isMinus)
            return -1*(hour * 3600 + min * 60 + sec);
        else
            return hour * 3600 + min * 60 + sec;
    }


    public static time sec2time(double s1)
    {
        time T = new time();
        double s;
        if (s1<0)// negative number
        {
            T.isMinus = true;
            s = -1 * s1;
        }
        else
        {
            T.isMinus = false;
            s = s1;
        }

        T.hour = Math.floor(s/3600);
        T.min = Math.floor((s - T.hour * 3600)/60);
        T.sec = s - T.hour * 3600 - T.min * 60 ;

        if (T.hour>999999999.0) // result too big to fit
        {
            T.hour = -1;
            T.min = -1;
            T.sec = -1;
        }
        return T;
    }

    public static time add(time A, time B)
    {
        time C = new time();
        C = sec2time(A.toSec() + B.toSec());
        return C;
    }

    public static time subtract(time A, time B)
    {
        time C = new time();
        C = sec2time(A.toSec() - B.toSec());
        return C;
    }

    public static time multiply(time A, double m)
    {
        time C = new time();
        C = sec2time(A.toSec() * m);
        return C;
    }

    public static time divide(time A, double m)
    {
        time C = new time();
        C = sec2time(A.toSec() / m);
        return C;
    }

    public static time performOperation(time A, time B, int Operation)
    {
        time C = new time();
        switch (Operation)
        {
            case Utility.plus:
                C=add(A,B);
                break;
            case Utility.minus:
                C = subtract(A,B);
                break;
            case Utility.equal:
                C = sec2time(B.toSec());
                break;
        }
        return C;
    }

    public static time performOperation(time A, double factor, int Operation)
    {
        time C = new time();
        switch (Operation)
        {
            case Utility.times:
                C = multiply(A,factor);
                break;
            case Utility.divide:
                C = divide(A,factor);
                break;
        }
        return C;
    }

    public double[] toArray()//Boolean isMinus)
    {
        double []res =new double[3];
        res[0] = this.hour;
        res[1] = this.min;
        res[2] = this.sec;
        return res;
    }

    public void reset()
    {
        this.hour = 0;
        this.min = 0;
        this.sec = 0;
        this.isMinus = false;
    }

    public String toStr()
    {
        String minusStr;
        if (isMinus)
            minusStr = "-";
        else
            minusStr = "";

        String str;
        if (sec == Math.floor(sec))
            str = minusStr+(int)hour+":"+(int)min+":"+(int)sec;
        else
            str = minusStr+(int)hour+":"+(int)min+":"+String.format("%.2f",sec);
        return str;
    }

    public static boolean isNaN(time T)
    {
        return (T.hour==-1)&(T.min==-1)&(T.sec==-1);
    }
}
