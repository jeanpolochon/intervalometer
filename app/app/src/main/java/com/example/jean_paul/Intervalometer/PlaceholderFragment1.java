package com.example.jean_paul.Intervalometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import static android.content.ContentValues.TAG;


/**
 * Created by jean-paul on 04/03/2018.
 */

public class PlaceholderFragment1 extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number1";
    private int delay;
    private View rootView;
    private Button buttonDelay;
    private Button buttonShoot;

    public PlaceholderFragment1() {
        delay=10;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FloatingActionButton settings = rootView.findViewById(R.id.settings1);
        settings.setOnClickListener(this);

        buttonDelay = rootView.findViewById(R.id.shootButtonLater);
        buttonDelay.setOnClickListener(this);

        buttonShoot = rootView.findViewById(R.id.shootButton);
        buttonShoot.setOnClickListener(this);


        return rootView;
    }

    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.settings1: {
                setDelay();
                break;
            }
            case R.id.shootButtonLater: {
                shootWithDelay();
                break;
            }
            case R.id.shootButton: {
                shoot();
                break;
            }
        }

    }

    private void setDelay() {
        Log.i(TAG, "onClick: set delay");
        final AlertDialog.Builder d = new AlertDialog.Builder(getView().getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);
        d.setTitle("Set delay in seconds");
        //d.setMessage("In seconds");
        d.setView(dialogView);
        final NumberPicker numberPicker = dialogView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setValue(delay);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick - delay set to " + numberPicker.getValue());
                delay = numberPicker.getValue();
                if (delay==1)
                {
                    buttonDelay.setText("SHOOT IN " + delay + " second");
                }
                else {
                    buttonDelay.setText("SHOOT IN " + delay + " seconds");
                }
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        //TODO: faire une alert dialog plus haute




        alertDialog.show();

    }

    private void shootWithDelay(){
        Log.i(TAG, "shootWithDelay");
    }

    private void shoot(){
        Log.i(TAG, "shoot");
    }


}
