package com.example.jean_paul.Intervalometer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.content.ContentValues.TAG;
import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by jean-paul on 04/03/2018.
 */

public class PlaceholderFragment2 extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number2";
    private int tot_min, tot_sec, num_pic, delay_min, delay_sec;
    Button launchButton;


    //initial state for the toggles - sortedID[1] and [2] will be selected by default
    int[] sortedID={R.id.toggleButtonNumPic,R.id.toggleButtonDelay, R.id.toggleButtonTotTime,};

    public PlaceholderFragment2() {
        tot_min=0;
        tot_sec=0;
        num_pic=10;
        delay_min=0;
        delay_sec=10;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView2 = inflater.inflate(R.layout.fragment_main2, container, false);

        launchButton = rootView2.findViewById(R.id.launch);
        launchButton.setOnClickListener(this);

        return rootView2;
    }


    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.launch: {
                launchIntervalometer();
                break;
            }
        }

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NumberPicker numberPicker_min = getView().findViewById(R.id.tot_min);
        numberPicker_min.setMaxValue(59);
        numberPicker_min.setMinValue(0);
        numberPicker_min.setValue(tot_min);
        numberPicker_min.setWrapSelectorWheel(true);
        numberPicker_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_sec = getView().findViewById(R.id.tot_sec);
        numberPicker_sec.setMaxValue(59);
        numberPicker_sec.setMinValue(0);
        numberPicker_sec.setValue(tot_sec);
        numberPicker_sec.setWrapSelectorWheel(true);
        numberPicker_sec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_num = getView().findViewById(R.id.num_pic);
        numberPicker_num.setMaxValue(200);
        numberPicker_num.setMinValue(1);
        numberPicker_num.setValue(num_pic);
        numberPicker_num.setWrapSelectorWheel(false);
        numberPicker_num.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_delay_min = getView().findViewById(R.id.delay_min);
        numberPicker_delay_min.setMaxValue(59);
        numberPicker_delay_min.setMinValue(0);
        numberPicker_delay_min.setValue(delay_min);
        numberPicker_delay_min.setWrapSelectorWheel(true);
        numberPicker_delay_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_delay_sec = getView().findViewById(R.id.delay_sec);
        numberPicker_delay_sec.setMaxValue(59);
        numberPicker_delay_sec.setMinValue(0);
        numberPicker_delay_sec.setValue(delay_sec);
        numberPicker_delay_sec.setWrapSelectorWheel(true);
        numberPicker_delay_sec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        ToggleButton toggle0 = getView().findViewById(sortedID[0]);
        ToggleButton toggle1 = getView().findViewById(sortedID[1]);
        toggle0.setChecked(true);
        toggle1.setChecked(true);
    }

    private void launchIntervalometer(){
        ToggleButton currentToggle;
        int numChecked = 0;
        for(int id : sortedID)
        {
            currentToggle=getView().findViewById(id);
            if (currentToggle.isChecked()==true)
                numChecked++;
        }
        if (numChecked!=2)
        {
            Log.i(TAG, "More than 2 options selected. Couldn't launch the intervalometer");
            Toast toast = Toast.makeText(PlaceholderFragment2.this.getContext(), "Please select only 2 options out of 3 above.", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Log.i(TAG, "Intervalometer launched");
        }
    }

}

