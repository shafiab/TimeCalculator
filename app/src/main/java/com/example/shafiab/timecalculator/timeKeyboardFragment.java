package com.example.shafiab.timecalculator;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by shafiab on 9/2/14.
 */
public class timeKeyboardFragment extends Fragment implements View.OnClickListener{
    String LOG_TAG = "Shafi Fragment_keyboardFragment";

    Button button;
    int[] buttons = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonPoint,
            R.id.buttonBack,
            R.id.buttonPlus, R.id.buttonMinus, R.id.buttonTimes, R.id.buttonDivide,
            R.id.buttonNext, R.id.buttonHr, R.id.buttonMin, R.id.buttonSec,
            R.id.buttonCE, R.id.buttonC,
            R.id.buttonEquals,
            R.id.buttonMR, R.id.buttonMC, R.id.buttonMplus,
            R.id.buttonPlusMinus};
    OnKeyPressListenerTime mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "inside onCreateView()");
        View view = inflater.inflate(R.layout.time_keyboard, container,false);
        for (int bt:buttons)
        {
            button = (Button) view.findViewById(bt);
            button.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        mListener.OnKeyPressTime(Id);
    }

    public interface OnKeyPressListenerTime {
        void OnKeyPressTime(int key);
    }



    @Override
    public void onAttach(Activity activity) {
        Log.i(LOG_TAG, "inside onAttach()");

        super.onAttach(activity);
        try {
            mListener = (OnKeyPressListenerTime) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnKeyPressListener");
        }
    }
}