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
public class factorUI{//} implements factorKeyboardFragment.OnKeyPressListenerFactor {
    String displayValue;
    double numericValue;
    Context context;
    boolean isPoint;
    int numB;
    factorDisplay myDisplay;
    boolean isMinus;
    double result;
    boolean memoryDisplayStatus;
    ContentResolver contentResolver;
    Cursor cursor;



    factorUI(Context context, factorDisplay myDisplay)
    {
        this.context = context;
        this.myDisplay = myDisplay;
        displayValue = "0";
        numericValue = 0;
        isPoint = false;
        numB =0;
        isMinus = false;
    }
    public void resetValue()
    {
        displayValue = "0";
        numericValue = 0;
        isPoint = false;
        numB =0;
        isMinus = false;
    }


    // save variables on rotation
    void save_factorUI(Bundle outState)
    {
        outState.putDouble("numericValue factorUI", numericValue);
        outState.putString("displayValue factorUI", displayValue);
        outState.putBoolean("isPoint factorUI", isPoint);
        outState.putInt("numB factorUI", numB);
        outState.putBoolean("isMinus factorUI", isMinus);
        outState.putBoolean("memoryDisplayStatus factorUI", memoryDisplayStatus);
    }

    // restore variables on rotation
    void restore_factorUI(Bundle outState)
    {
        numericValue = outState.getDouble("numericValue factorUI");
        displayValue = outState.getString("displayValue factorUI");
        isPoint = outState.getBoolean("isPoint factorUI");
        numB = outState.getInt("numB factorUI");
        isMinus = outState.getBoolean("isMinus factorUI");
        memoryDisplayStatus = outState.getBoolean("memoryDisplayStatus factorUI");
    }

    // restore view
    void restore_factorDisplay()
    {
        myDisplay.UpdateText(displayValue, isMinus, R.color.color_red);
        myDisplay.updateMemoryDisplay(memoryDisplayStatus);
    }


    public boolean OnKeyPressFactor(int key)
    {
        String pressedKey = Utility.convertToDisplayDigit(key);
        boolean isFinished = false;
        if (pressedKey=="Other") // not a number
        {
            isFinished = selectOtherActivities(key);
        }
        else // number or point, update value
        {
            updateDigit(pressedKey, false, false);
        }
        myDisplay.UpdateText(displayValue, isMinus, R.color.color_red);


        if (isFinished)
        {
            if (isMinus)
                result = -1*numericValue;
            else
                result = numericValue;
        }
        return isFinished;
    }

    double getFactor()
    {
        return result;
    }

    void updateDigit(String pressedKey, boolean backButton, boolean clear)
    {
        if (backButton)
        {
            if (displayValue.length()>1)
            {
                char value = displayValue.charAt(displayValue.length() - 1);

                if (value == '.')
                {
                    isPoint = false;
                    numB = 0;
                }
                if (isPoint)
                    numB--;

                displayValue = displayValue.substring(0, displayValue.length() - 1);
                numericValue = Double.parseDouble(displayValue);
            }
            else if (displayValue.length()==1)
            {
                displayValue = "0";
                numericValue = 0;
                isPoint = false;
                numB = 0;
            }
        }
        else if (clear)
        {
            displayValue = "0";
            numericValue = 0;
            isMinus = false;
            isPoint = false;
            numB = 0;
        }
        else
        {

            if (pressedKey == ".") // point
            {
                if (!isPoint) //first time point
                {
                    numericValue = Double.parseDouble(displayValue);
                    displayValue = displayValue + pressedKey;
                    isPoint = true;
                }
            }
            else //number
            {
                if (displayValue.equals("0"))
                    displayValue = "";

                if ((!isPoint & (numericValue<=999))|(isPoint&numB<2))
                {
                    displayValue = displayValue + pressedKey;
                    numericValue = Double.parseDouble(displayValue);
                    if (isPoint)
                        numB++;
                }
            }
        }
    }


    boolean selectOtherActivities(int key)
    {
        boolean flag = false;
        switch(key)
        {
            case R.id.buttonBack:
                updateDigit("", true, false);
                break;
            case R.id.buttonCE:
                updateDigit("",false,true);
                break;
            case R.id.buttonC:
                numericValue = 1; // to avoid any error condition
                flag = true;
                // will go back to timeUI
                break;
            case R.id.buttonPlus:
                flag = true;
                // will go back to timeUI
                break;
            case R.id.buttonMinus:
                flag = true;
                // will go back to timeUI
                break;
            case R.id.buttonEquals:
                flag = true;
                // will go back to timeUI
                break;
            case R.id.buttonPlusMinus:
                if (isMinus)
                    isMinus = false;
                else
                    isMinus = true;

                //myDisplay.setMinus(isMinus);
                break;

            case R.id.buttonMC:
                contentResolver = context.getContentResolver();
                contentResolver.delete(TimeContract.FactorEntry.CONTENT_URI, null, null);

                memoryDisplayStatus = false;
                myDisplay.updateMemoryDisplay(memoryDisplayStatus);
                break;

            case R.id.buttonMR:
                contentResolver = context.getContentResolver();
                cursor = contentResolver.query(TimeContract.FactorEntry.CONTENT_URI, null, null, null, null);
                if (cursor.getCount()!=0)
                {
                    cursor.moveToFirst();
                    double tempValue = cursor.getDouble(cursor.getColumnIndex(TimeContract.FactorEntry.COLUMN_FACTOR));
                    if (tempValue>=0)
                    {
                        numericValue = tempValue;
                        isMinus = false;
                    }
                    else if (tempValue<0)
                    {
                        numericValue = -1*tempValue;
                        isMinus = true;
                    }
                    numB = Utility.getNumberDigits(numericValue);
                    if (numB==2)
                        displayValue = String.format("%.2f",numericValue);
                    else if (numB ==1)
                        displayValue = String.format("%.1f",numericValue);
                    else
                        displayValue = String.format("%.0f",numericValue);
                    if (numB>0)
                        isPoint = true;
                }
                else //Memory is empty show 0
                {
                    numericValue = 0;
                    displayValue = "0";
                    isPoint = false;
                    isMinus = false;
                    numB = 0;
                }

                break;
            case R.id.buttonMplus:
                // first get the existing value
                double tempVal = 0;
                cursor = context.getContentResolver().query(TimeContract.FactorEntry.CONTENT_URI, null, null, null, null);

                if (cursor.getCount()!=0)
                {
                    cursor.moveToFirst();
                    tempVal = cursor.getDouble(cursor.getColumnIndex(TimeContract.FactorEntry.COLUMN_FACTOR));
                }
                if (isMinus)
                    tempVal = tempVal-numericValue;
                else
                    tempVal = tempVal+numericValue;

                //time tempTime1 = new time(numericValue, isMinus);
                contentResolver = context.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(TimeContract.FactorEntry.COLUMN_FACTOR,tempVal);
                //contentResolver.update(TimeContract.TimeEntry.CONTENT_URI, values,null,null);
                if (cursor.getCount()!=0)
                    contentResolver.update(TimeContract.FactorEntry.CONTENT_URI,values,null,null);
                else //first entry
                    contentResolver.insert(TimeContract.FactorEntry.CONTENT_URI, values);

                memoryDisplayStatus = true;
                myDisplay.updateMemoryDisplay(memoryDisplayStatus);
                break;
            default:
                break;
        }
        return flag;

    }

    void saveDataFutureSession( SharedPreferences.Editor outState)
    {
        outState.putBoolean("memoryDisplayStatus factorUI", memoryDisplayStatus);

    }

    void restoreDataFutureSession(SharedPreferences outState)
    {
        memoryDisplayStatus = outState.getBoolean("memoryDisplayStatus factorUI",false);
    }

}

