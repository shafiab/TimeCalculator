package com.example.shafiab.timecalculator;

/**
 * Created by shafiab on 9/3/14.
 */
public class Utility {
    final static public int hour = 0;
    final static public int minute = 1;
    final static public int second = 2;
    final static public int plusminus = 3;


    final static public int plus = 0;
    final static public int minus = 1;
    final static public int times = 2;
    final static public int divide = 3;
    final static public int equal = 4;
    final static public int clear = 5;


    public static String convertToDisplayDigit(int Id)
    {
        String myStr = null;
        switch (Id)
        {
            case R.id.button0:
                myStr = "0";
                break;
            case R.id.button1:
                myStr = "1";
                break;
            case R.id.button2:
                myStr = "2";
                break;
            case R.id.button3:
                myStr = "3";
                break;
            case R.id.button4:
                myStr = "4";
                break;
            case R.id.button5:
                myStr = "5";
                break;
            case R.id.button6:
                myStr = "6";
                break;
            case R.id.button7:
                myStr = "7";
                break;
            case R.id.button8:
                myStr = "8";
                break;
            case R.id.button9:
                myStr = "9";
                break;
            case R.id.buttonPoint:
                myStr =".";
                break;
            default:
                myStr = "Other";
                break;
        }
        return myStr;
    }

    public static String convertToDisplayDigitOther(int Id) {
        String myStr = null;
        switch (Id) {
            case R.id.buttonPlus:
                myStr = "+";
                break;
            case R.id.buttonMinus:
                myStr = "-";
                break;
            case R.id.buttonTimes:
                myStr = "\u00d7";
                break;
            case R.id.buttonDivide:
                myStr = "\u00f7";
                break;
            case R.id.buttonEquals:
                myStr = "=";
                break;
            case R.id.buttonC:
                myStr = "C";
            default:
                break;
        }
        return myStr;
    }


        public static int KeyToOperation(int key)
    {
        int operation = -1;
        switch (key)
        {
            case R.id.buttonTimes:
                operation = Utility.times;
                break;
            case R.id.buttonDivide:
                operation = Utility.divide;
                break;
            case R.id.buttonPlus:
                operation = Utility.plus;
                break;
            case R.id.buttonMinus:
                operation = Utility.minus;
                break;
            case R.id.buttonEquals:
                operation = Utility.equal;
                break;
            case R.id.buttonC:
                operation = Utility.clear;
            default:
                break;
        }
        return operation;
    }

    static int getNumberDigits(double d)
    {
        int point;
        String text = Double.toString(Math.abs(d));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        point = 0;
        switch(decimalPlaces)
        {
            case 0:
                break;
            case 1:
                if (text.charAt(1+integerPlaces) != '0')
                    point = 1;
                break;
            case 2:
                if (text.charAt(2+integerPlaces) != '0')
                    point = 2;
                else if (text.charAt(1+integerPlaces) != '0')
                    point = 1;
                break;
            default:
                break;
        }
        return  point;

    }
}
