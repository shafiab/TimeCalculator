package com.example.shafiab.timecalculator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shafiab on 9/2/14.
 */
public class timeDisplay extends Fragment {
    String LOG_TAG = "Shafi Fragment_timeDisplay";
    TimeDisplayInterface mTimeDisplayInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "inside onCreateView()");
        View view = inflater.inflate(R.layout.display_time, container,false);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mTimeDisplayInterface.setTimeDisplay();
    }

    public void updatePrimaryDisplayModule(String text, int window, int color1)
    {
        int color = getResources().getColor(color1);
        TextView myText;
        switch(window)
        {
            case Utility.hour:
                myText = (TextView) getView().findViewById(R.id.textViewHour);
                myText.setTextColor(color);
                myText.setText(text);
                break;
            case Utility.minute:
                myText = (TextView) getView().findViewById(R.id.textViewMinute);
                myText.setTextColor(color);
                myText.setText(text);
                break;
            case Utility.second:
                myText = (TextView) getView().findViewById(R.id.textViewSecond);
                myText.setTextColor(color);
                myText.setText(text);
                break;
            case Utility.plusminus:
                myText = (TextView) getView().findViewById(R.id.textViewPlusMinus);
                myText.setTextColor(color);
                myText.setText(text);
                break;
            default:
                break;
        }

    }

    public void updatePrimaryDisplay(String[] displayTime, boolean isMinus, int iterator)
    {
        for (int i=0;i<3;i++)
        {
            if (i==iterator)
                updatePrimaryDisplayModule(displayTime[i], i, R.color.color_red);
            else
                updatePrimaryDisplayModule(displayTime[i], i, R.color.color_white);
        }

        if(isMinus)
            updatePrimaryDisplayModule("-", 3, R.color.color_white);
        else
            updatePrimaryDisplayModule("", 3, R.color.color_white);
    }

    public void updateMemoryDisplay(boolean status)
    {
        TextView myText = (TextView)getView().findViewById(R.id.textViewMemory);
        myText.setTextColor(getResources().getColor(R.color.color_white));
        if (status)
            myText.setText("M");
        else
            myText.setText("");

    }

    public void updateSecondaryDisplay(String resultTime, String operation)
    {
        TextView myText;
        myText = (TextView) getView().findViewById(R.id.textViewIntermediate);
        myText.setTextColor(getResources().getColor(R.color.color_white));
        myText.setText(resultTime);

        myText = (TextView) getView().findViewById(R.id.textViewSign);
        myText.setTextColor(getResources().getColor(R.color.color_white));
        myText.setText(operation);

    }

    public interface TimeDisplayInterface {
        void setTimeDisplay();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(LOG_TAG, "inside onAttach()");

        super.onAttach(activity);
        try {
            mTimeDisplayInterface = (TimeDisplayInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TimeDisplayInterface");
        }
    }

}
