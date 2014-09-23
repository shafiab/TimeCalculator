package com.example.shafiab.timecalculator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shafiab on 9/4/14.
 */
public class factorDisplay extends Fragment{
    String intermediateTime;
    String intermediateOperation;
    boolean memoryDisplayStatus;

    FactorDisplayInterface mFactorDisplayInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_factor,container,false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mFactorDisplayInterface.setFactorDisplay();
        updateIntermediateText();
    }

    public void UpdateText(String text, boolean isMinus, int color1) {
        int color = getResources().getColor(color1);
        TextView myText;
        myText = (TextView) getView().findViewById(R.id.textViewFactorDisplay);
        myText.setTextColor(color);
        myText.setText(text);

        myText = (TextView) getView().findViewById(R.id.textViewFactorPlusMinus);
        myText.setTextColor(color);
        if (isMinus)
            myText.setText("-");
        else
            myText.setText("");


    }

    public void updateIntermediateText()
    {
        TextView myText;
        myText = (TextView) getView().findViewById(R.id.textViewIntermediate);
        myText.setTextColor(getResources().getColor(R.color.color_white));
        myText.setText(intermediateTime);

        myText = (TextView) getView().findViewById(R.id.textViewSign);
        myText.setTextColor(getResources().getColor(R.color.color_white));
        myText.setText(intermediateOperation);

    }

    public void  setIntermediateValue(String intermediateTime, String intermediateOperation)
    {
        this.intermediateOperation = intermediateOperation;
        this.intermediateTime = intermediateTime;
    }

    void updateMemoryDisplay(boolean status)
    {
        TextView myText = (TextView)getView().findViewById(R.id.textViewFactorMemory);
        myText.setTextColor(getResources().getColor(R.color.color_white));
        if (status)
            myText.setText("Mfactor");
        else
            myText.setText("");
    }

    public interface FactorDisplayInterface {
        void setFactorDisplay();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFactorDisplayInterface = (FactorDisplayInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TimeDisplayInterface");
        }
    }

}