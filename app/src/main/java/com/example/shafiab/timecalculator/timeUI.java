package com.example.shafiab.timecalculator;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import com.example.shafiab.timecalculator.data.TimeContract;

/**
 * Created by shafiab on 9/4/14.
 */
public class timeUI {
    Context context;
    String[] displayValue;
    double[] numericValue;
    boolean[] isPoint;
    int[] numB;
    int iterator;
    time resultTime;
    timeDisplay myDisplay;
    int operator;
    boolean isNan;
    boolean memoryDisplayStatus;
    Cursor cursor;
    boolean isMinus;

    ContentResolver contentResolver;

    String secondaryDisplayTime;
    String secondaryDisplayOperator;



    timeUI(Context context, timeDisplay myDisplay)
    {
        this.context = context;
        this.myDisplay = myDisplay;
        numericValue = new double[]{0,0,0};
        displayValue = new String[]{"0","0","0"};
        isPoint = new boolean[]{false,false,false};
        numB = new int[]{0,0,0};
        iterator = 0;
        resultTime = new time();
        operator = Utility.equal;
        isNan = false;
        secondaryDisplayTime = "";
        secondaryDisplayOperator = "";
        memoryDisplayStatus = false;
        isMinus = false;

    }

    // restore variables on rotation
    void restore_timeUI(Bundle outState)
    {
        numericValue = outState.getDoubleArray("numericValue timeUI");
        displayValue = outState.getStringArray("displayValue timeUI");
        isPoint = outState.getBooleanArray("isPoint timeUI");
        numB = outState.getIntArray("numB timeUI");
        iterator = outState.getInt("iterator timeUI");
        operator = outState.getInt("operator timeUI");
        isNan = outState.getBoolean("isNan timeUI");
        isMinus = outState.getBoolean("isMinus timeUI");
        double[] time = outState.getDoubleArray("resultTime timeUI");
        resultTime.setTime(time[0],time[1],time[2],outState.getBoolean("resultTime isMinus timeUI"));
        secondaryDisplayTime = outState.getString("secondaryDisplayTime timeUI");
        secondaryDisplayOperator = outState.getString("secondaryDisplayOperator timeUI");
        memoryDisplayStatus = outState.getBoolean("memoryDisplayStatus  timeUI");
    }

    // restore view
    void restore_timeDisplay()
    {
        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator);
        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
        myDisplay.updateMemoryDisplay(memoryDisplayStatus);
    }

    // save variables on rotation
    void save_timeUI(Bundle outState)
    {
        outState.putDoubleArray("numericValue timeUI", numericValue);
        outState.putStringArray("displayValue timeUI", displayValue);
        outState.putBooleanArray("isPoint timeUI", isPoint);
        outState.putIntArray("numB timeUI", numB);
        outState.putInt("iterator timeUI", iterator);
        outState.putInt("operator timeUI", operator);
        outState.putBoolean("isNan timeUI", isNan);
        outState.putBoolean("isMinus timeUI", isMinus);

        outState.putDoubleArray("resultTime timeUI",resultTime.toArray());
        outState.putBoolean("resultTime isMinus timeUI",resultTime.isMinus);

        outState.putString("secondaryDisplayTime timeUI",secondaryDisplayTime);
        outState.putString("secondaryDisplayOperator timeUI",secondaryDisplayOperator);
        outState.putBoolean("memoryDisplayStatus  timeUI", memoryDisplayStatus);
    }


    void setValue(time myT, int operation, boolean isNan) // called after exiting factor display
    {
        if (isNan) // special case of nan
        {
            ResetAndClear();
            displayValue[0] = "Err";
            displayValue[1] = "Err";
            displayValue[2] = "Err";
            secondaryDisplayTime = "";
            secondaryDisplayOperator = "";
            this.isNan = isNan;
        }
        else
        {
            if (operation==Utility.clear)
            {
                ResetAndClear();
                resultTime.reset();
                updateParametersFromResult(myT);
                operator = Utility.equal;
                secondaryDisplayTime = "";
                secondaryDisplayOperator = "";
            }
            else if(operation == Utility.equal)
            {
                ResetAndClear();
                resultTime.setTime(myT);
                updateParametersFromResult(myT);
                operator = operation;
                secondaryDisplayTime = "";
                secondaryDisplayOperator = "=";
            }
            else if(operation == Utility.plus) // plus or minus
            {
                ResetAndClear();
                resultTime.setTime(myT);
                operator = operation;
                secondaryDisplayTime = resultTime.toStr();
                secondaryDisplayOperator = "+";
            }
            else if(operation == Utility.minus) // plus or minus
            {
                ResetAndClear();
                resultTime.setTime(myT);
                operator = operation;
                secondaryDisplayTime = resultTime.toStr();
                secondaryDisplayOperator = "-";
            }

        }
    }

    boolean OnKeyPressTime(int key, time myT)
    {
        boolean flag = false;
        String pressedKey = Utility.convertToDisplayDigit(key);

        if (pressedKey=="Other") // not a number
        {
            //perform activities corresponding to key
            flag = performOtherActivities(key);
        }
        else // number or point, update value
        {
            if (!isNan)
            {
                updateDigit(pressedKey, false, false);
                myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
            }
        }

        if (flag) myT.setTime(resultTime);

        return flag;
    }


    void updateDigit(String pressedKey, boolean backButton, boolean clear)
    {
        if (backButton)
        {
            if (displayValue[iterator].length()>1)
            {
                char value = displayValue[iterator].charAt(displayValue[iterator].length() - 1);

                if (value == '.')
                {
                    isPoint[iterator] = false;
                    numB[iterator] = 0;
                }
                if (isPoint[iterator])
                    numB[iterator]--;

                displayValue[iterator] = displayValue[iterator].substring(0, displayValue[iterator].length() - 1);
                numericValue[iterator] = Double.parseDouble(displayValue[iterator]);
            }
            else if (displayValue[iterator].length()==1)
            {
                displayValue[iterator] = "0";
                numericValue[iterator] = 0;
                isPoint[iterator] = false;
                numB[iterator] = 0;
            }
        }
        else if (clear)
        {
            displayValue[iterator] = "0";
            numericValue[iterator] = 0;
            isPoint[iterator] = false;
            numB[iterator] = 0;
        }
        else
        {

            if (pressedKey == ".") // point
            {
                if (!isPoint[iterator]) //first time point
                {
                    numericValue[iterator] = Double.parseDouble(displayValue[iterator]);
                    displayValue[iterator] = displayValue[iterator] + pressedKey;
                    isPoint[iterator] = true;
                }
            }
            else //number
            {
                if (displayValue[iterator].equals("0"))
                    displayValue[iterator] = "";

                if ((!isPoint[iterator] & (numericValue[iterator]<=999))|(isPoint[iterator]&numB[iterator]<2))
                {
                    displayValue[iterator] = displayValue[iterator] + pressedKey;
                    numericValue[iterator] = Double.parseDouble(displayValue[iterator]);
                    if (isPoint[iterator])
                        numB[iterator]++;
                }
            }
        }
    }


    boolean performOtherActivities(int key)
    {
        boolean flag = false;
        if (!isNan) {
            switch (key) {
                case R.id.buttonBack:
                    updateDigit("", true, false);
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
                    break;
                case R.id.buttonNext:
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_white);
                    iterator = (iterator + 1) % 3;
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
                    break;
                case R.id.buttonHr:
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_white);
                    iterator = Utility.hour;
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
                    break;
                case R.id.buttonMin:
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_white);
                    iterator = Utility.minute;
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
                    break;
                case R.id.buttonSec:
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_white);
                    iterator = Utility.second;
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
                    break;
                case R.id.buttonCE:
                    updateDigit("", false, true);
                    myDisplay.updatePrimaryDisplayModule(displayValue[iterator], iterator, R.color.color_red);
                    break;
                case R.id.buttonC:
                    resultTime.reset(); // reset the result
                    ResetAndClear(); // reset other variable
                    myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator); // update primary display
                    secondaryDisplayTime = "";
                    secondaryDisplayOperator = "";
                    myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);

                   // context.getContentResolver().delete(TimeContract.TIME_URI, null, null);
                   // memoryDisplayStatus = false;
                   // myDisplay.updateMemoryDisplay(memoryDisplayStatus);

                    break;
                case R.id.buttonPlus:
                    resultTime = time.performOperation(resultTime, new time(numericValue, isMinus), operator);
                    if (time.isNaN(resultTime))
                    {
                        setValue(resultTime, Utility.plus, true);
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator);
                        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
                    }
                    else
                    {
                        ResetAndClear();
                        operator = Utility.plus;
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator); // update primary display
                        secondaryDisplayTime = resultTime.toStr();
                        secondaryDisplayOperator = Utility.convertToDisplayDigitOther(R.id.buttonPlus);
                        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
                    }
                    break;
                case R.id.buttonMinus:
                    resultTime = time.performOperation(resultTime, new time(numericValue, isMinus), operator);
                    if (time.isNaN(resultTime))
                    {
                        setValue(resultTime, Utility.plus, true);
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator);
                        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
                    }
                    else
                    {
                        ResetAndClear();
                        operator = Utility.minus;
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator); // update primary display
                        secondaryDisplayTime = resultTime.toStr();
                        secondaryDisplayOperator = Utility.convertToDisplayDigitOther(R.id.buttonMinus);
                        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
                    }
                    break;
                case R.id.buttonTimes:
                    resultTime = time.performOperation(resultTime, new time(numericValue, isMinus), operator);
                    ResetAndClear();
                    flag = true;
                    break;
                case R.id.buttonDivide:
                    resultTime = time.performOperation(resultTime, new time(numericValue, isMinus), operator);
                    ResetAndClear();
                    flag = true;
                    break;
                case R.id.buttonEquals:
                    resultTime = time.performOperation(resultTime, new time(numericValue,isMinus), operator);
                    if (time.isNaN(resultTime))
                    {
                        setValue(resultTime, Utility.plus, true);
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator);
                        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
                    }
                    else
                    {
                        updateParametersFromResult(resultTime);
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator); // update primary display
                        secondaryDisplayTime = "";
                        secondaryDisplayOperator = Utility.convertToDisplayDigitOther(R.id.buttonEquals);
                        myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
                    }
                    break;
                case R.id.buttonMC:
                    contentResolver = context.getContentResolver();
                    contentResolver.delete(TimeContract.TimeEntry.CONTENT_URI, null, null);

                    memoryDisplayStatus = false;
                    myDisplay.updateMemoryDisplay(memoryDisplayStatus);
                    break;
                case R.id.buttonMR:
                    contentResolver = context.getContentResolver();
                    cursor = contentResolver.query(TimeContract.TimeEntry.CONTENT_URI, null, null, null, null);
                    if (cursor.getCount()!=0)
                    {
                        cursor.moveToFirst();
                        int tOperator = operator;
                        ResetAndClear();
                        numericValue[0] = cursor.getDouble(1);
                        numericValue[1] = cursor.getDouble(2);
                        numericValue[2] = cursor.getDouble(3);

                        isMinus = (cursor.getInt(4)>0);
                        updateParametersFromResult(new time(numericValue, isMinus));
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator); // update primary display
                        operator = tOperator;
                    }
                    else //Memory is empty show 0
                    {
                        int tOperator = operator;
                        ResetAndClear();
                        numericValue[0] = 0;
                        numericValue[1] = 0;
                        numericValue[2] = 0;

                        isMinus = false;
                        updateParametersFromResult(new time(numericValue, isMinus));
                        myDisplay.updatePrimaryDisplay(displayValue, isMinus, iterator); // update primary display
                        operator = tOperator;


                    }

                    break;
                case R.id.buttonMplus:
                    // first get the existing value


                    double []tempTime = {0,0,0};
                    boolean tempisMinus = false;
                    cursor = context.getContentResolver().query(TimeContract.TimeEntry.CONTENT_URI, null, null, null, null);

                    if (cursor.getCount()!=0)
                    {
                        cursor.moveToFirst();
                        tempTime[0] = cursor.getDouble(cursor.getColumnIndex(TimeContract.TimeEntry.COLUMN_HR));
                        tempTime[1] = cursor.getDouble(cursor.getColumnIndex(TimeContract.TimeEntry.COLUMN_MIN));
                        tempTime[2] = cursor.getDouble(cursor.getColumnIndex(TimeContract.TimeEntry.COLUMN_SEC));
                        tempisMinus = (cursor.getDouble(cursor.getColumnIndex(TimeContract.TimeEntry.COLUMN_isMIN))>0);
                    }

                    time tempTime1 = time.performOperation(new time(numericValue, isMinus), new time(tempTime, tempisMinus), Utility.plus);

                    //time tempTime1 = new time(numericValue, isMinus);
                    contentResolver = context.getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(TimeContract.TimeEntry.COLUMN_HR,tempTime1.hour);
                    values.put(TimeContract.TimeEntry.COLUMN_MIN,tempTime1.min);
                    values.put(TimeContract.TimeEntry.COLUMN_SEC,tempTime1.sec);
                    values.put(TimeContract.TimeEntry.COLUMN_isMIN,tempTime1.isMinus);
                    //contentResolver.update(TimeContract.TimeEntry.CONTENT_URI, values,null,null);
                    if (cursor.getCount()!=0)
                        contentResolver.update(TimeContract.TimeEntry.CONTENT_URI,values,null,null);
                    else //first entry
                        contentResolver.insert(TimeContract.TimeEntry.CONTENT_URI, values);

                    memoryDisplayStatus = true;
                    myDisplay.updateMemoryDisplay(memoryDisplayStatus);
                    break;
                //case R.id.buttonMminus:
                    //memoryDisplayStatus = true;
                    //myDisplay.updateMemoryDisplay(memoryDisplayStatus);
                    //break;
                case R.id.buttonPlusMinus:
                    if (isMinus)
                    {
                        isMinus = false;
                        myDisplay.updatePrimaryDisplayModule("",3,R.color.color_white);
                    }
                    else
                    {
                        isMinus = true;
                        myDisplay.updatePrimaryDisplayModule("-",3,R.color.color_white);
                    }
                    break;

                default:
                    break;
            }
        }
        else if (isNan & (key==R.id.buttonC))
        {
            resultTime.reset(); // reset the result
            ResetAndClear(); // reset other variable
            myDisplay.updatePrimaryDisplay(displayValue,isMinus, iterator); // update primary display
            secondaryDisplayTime = "";
            secondaryDisplayOperator = "";
            myDisplay.updateSecondaryDisplay(secondaryDisplayTime, secondaryDisplayOperator);
        }
        return flag;
    }

    void ResetAndClear() // reset Display and Numeric
    {
        isNan = false;
        isMinus = false;
        for (iterator=0;iterator<3;iterator++) {
            updateDigit("", false, true);
        }
        iterator = 0;
        operator = Utility.equal;
    }

    void updateParametersFromResult(time myTime)
    {
        isMinus = myTime.isMinus;
        numericValue = myTime.toArray();
        for (iterator=0;iterator<2;iterator++)
        {
            isPoint[iterator] = false;
            numB[iterator] = 0;
            displayValue[iterator] = String.format("%.0f",numericValue[iterator]);
        }
        if ((numericValue[2]%1)==0) {
            isPoint[2] = false;
            numB[2] = 0;
            displayValue[2] = String.format("%.0f",numericValue[2]);
        }
        else
        {
            isPoint[2] = true;
            double fraction = numericValue[2] - Math.floor(numericValue[2]);
            if(((fraction*100)%10) == 0)
            {
                numB[2] = 1;
                displayValue[2] = String.format("%.1f",numericValue[2]);
            }
            else
            {
                numB[2] = 2;
                displayValue[2] = String.format("%.2f",numericValue[2]);
            }
        }
        iterator = 0;
        operator = Utility.equal;
    }




    void saveDataFutureSession( SharedPreferences.Editor outState)
    {
        outState.putString("numericValue timeUI hr", String.valueOf(numericValue[0]));
        outState.putString("numericValue timeUI min", String.valueOf(numericValue[1]));
        outState.putString("numericValue timeUI sec", String.valueOf(numericValue[2]));

        outState.putString("displayValue timeUI hr", displayValue[0]);
        outState.putString("displayValue timeUI min", displayValue[1]);
        outState.putString("displayValue timeUI sec", displayValue[2]);

        outState.putBoolean("isPoint timeUI hr", isPoint[0]);
        outState.putBoolean("isPoint timeUI min", isPoint[1]);
        outState.putBoolean("isPoint timeUI sec", isPoint[2]);

        outState.putInt("numB timeUI hr", numB[0]);
        outState.putInt("numB timeUI min", numB[1]);
        outState.putInt("numB timeUI sec", numB[2]);

        outState.putInt("iterator timeUI", iterator);
        outState.putInt("operator timeUI", operator);
        outState.putBoolean("isNan timeUI", isNan);
        outState.putBoolean("isMinus timeUI",isMinus);
        outState.putBoolean("memoryDisplayStatus timeUI", memoryDisplayStatus);

        outState.putString("resultTime timeUI hr",Double.toString(resultTime.hour));
        outState.putString("resultTime timeUI min",Double.toString(resultTime.min));
        outState.putString("resultTime timeUI sec",Double.toString(resultTime.sec));
        outState.putBoolean("resultTime timeUI isMinus", resultTime.isMinus);

        outState.putString("secondaryDisplayTime timeUI",secondaryDisplayTime);
        outState.putString("secondaryDisplayOperator timeUI",secondaryDisplayOperator);
    }

    void restoreDataFutureSession(SharedPreferences outState)
    {
        numericValue[0] = Double.valueOf(outState.getString("numericValue timeUI hr", "0"));
        numericValue[1] = Double.valueOf(outState.getString("numericValue timeUI min", "0"));
        numericValue[2] = Double.valueOf(outState.getString("numericValue timeUI sec", "0"));

        displayValue[0] = outState.getString("displayValue timeUI hr","0");
        displayValue[1] = outState.getString("displayValue timeUI min","0");
        displayValue[2] = outState.getString("displayValue timeUI sec", "0");

        isPoint[0] = outState.getBoolean("isPoint timeUI hr", false);
        isPoint[1] = outState.getBoolean("isPoint timeUI min", false);
        isPoint[2] = outState.getBoolean("isPoint timeUI sec", false);
        memoryDisplayStatus = outState.getBoolean("memoryDisplayStatus timeUI",false);

        numB[0] = outState.getInt("numB timeUI hr", 0);
        numB[1] = outState.getInt("numB timeUI min", 0);
        numB[2] = outState.getInt("numB timeUI sec", 0);

        iterator = outState.getInt("iterator timeUI", 0);
        operator = outState.getInt("operator timeUI", 0);
        isNan = outState.getBoolean("isNan timeUI", false);
        isMinus = outState.getBoolean("isMinus timeUI", false);

        resultTime.hour = Double.valueOf(outState.getString("resultTime timeUI hr", "0"));
        resultTime.min = Double.valueOf(outState.getString("resultTime timeUI min", "0"));
        resultTime.sec = Double.valueOf(outState.getString("resultTime timeUI sec", "0"));
        resultTime.isMinus = outState.getBoolean("resultTime timeUI isMinus", false);

        secondaryDisplayTime = outState.getString("secondaryDisplayTime timeUI","");
        secondaryDisplayOperator = outState.getString("secondaryDisplayOperator timeUI","");
    }


}
