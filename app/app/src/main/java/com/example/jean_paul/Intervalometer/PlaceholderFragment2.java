package com.example.jean_paul.Intervalometer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by jean-paul on 04/03/2018.
 */

public class PlaceholderFragment2 extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number2";

    public PlaceholderFragment2() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment2 newInstance(int sectionNumber) {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView2 = inflater.inflate(R.layout.fragment_main2, container, false);

        return rootView2;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] data = new String[]{"Berlin", "Moscow", "Tokyo", "Paris"};
        /*NumberPicker picker = (NumberPicker) getView().findViewById(R.id.number_picker);
        picker.setMinValue(0);
        picker.setMaxValue(data.length-1);
        picker.setDisplayedValues(data);*/

        final NumberPicker numberPicker_min = (NumberPicker) getView().findViewById(R.id.tot_min);
        numberPicker_min.setMaxValue(59);
        numberPicker_min.setMinValue(0);
        //numberPicker_min.setValue(delay);
        numberPicker_min.setWrapSelectorWheel(true);
        numberPicker_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_sec = (NumberPicker) getView().findViewById(R.id.tot_sec);
        numberPicker_sec.setMaxValue(59);
        numberPicker_sec.setMinValue(0);
        //numberPicker_sec.setValue(delay);
        numberPicker_sec.setWrapSelectorWheel(true);
        numberPicker_sec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_num = (NumberPicker) getView().findViewById(R.id.num_pic);
        numberPicker_num.setMaxValue(200);
        numberPicker_num.setMinValue(1);
        //numberPicker_num.setValue(delay);
        numberPicker_num.setWrapSelectorWheel(false);
        numberPicker_num.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_delay_min = (NumberPicker) getView().findViewById(R.id.delay_min);
        numberPicker_delay_min.setMaxValue(59);
        numberPicker_delay_min.setMinValue(0);
        //numberPicker_delay_min.setValue(delay);
        numberPicker_delay_min.setWrapSelectorWheel(true);
        numberPicker_delay_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker numberPicker_delay_sec = (NumberPicker) getView().findViewById(R.id.delay_sec);
        numberPicker_delay_sec.setMaxValue(59);
        numberPicker_delay_sec.setMinValue(0);
        //numberPicker_delay_sec.setValue(delay);
        numberPicker_delay_sec.setWrapSelectorWheel(true);
        numberPicker_delay_sec.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        WindowManager windowManager = (WindowManager)getContext().getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();

    }
}