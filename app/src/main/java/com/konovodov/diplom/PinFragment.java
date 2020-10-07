package com.konovodov.diplom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PinFragment extends Fragment implements PinStore {


    private View fragmentView;
    private int fragmentState;
    private String screenHeader;
    private StringBuilder enteredPin = new StringBuilder();
    private final int MAX_PIN_DIGITS = 4;
    public OnPinEntered onPinEntered = null; //это объект интерфейса, выполняющий действия
    // при наборе полного пинкода. В некоторых случаях это проверка,
    // в некоторых - получение нового пина, в других - подтверждение пинкода для его сброса

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentState = getArguments().getInt("fragmentState", 0);
        fragmentState = getArguments().getInt("fragmentState", 0);


    }
    public static PinFragment getInstance(int state, OnPinEntered onPinEntered) {
        PinFragment fragment = new PinFragment();
        fragment.setOnPinEntered(onPinEntered);
        Bundle args = new Bundle();
        args.putInt("fragmentState", state);
        fragment.setArguments(args);
        return fragment;
    }

    //этот метод устанавливает требуемый коллбэк при завершении ввода пинкода.
    //а сам коллбэк делается из интерфейса OnPinEntered
    public void setOnPinEntered(OnPinEntered onPinEntered) {
        this.onPinEntered = onPinEntered;
    }


    public String getEnteredPin() {
        if (enteredPin.length() < MAX_PIN_DIGITS) return null;
        return enteredPin.toString();
    }


    private void addDigitToPin(char symbol) {
        if (enteredPin.length() < MAX_PIN_DIGITS) enteredPin.append(symbol);
        pinBarRefresh(enteredPin.length());
    }

    private void removeDigitFromPin() {
        int enteredPinKeyLength = enteredPin.length();
        if (enteredPinKeyLength == 0) return;
        enteredPin.deleteCharAt(enteredPinKeyLength - 1);
        pinBarRefresh(--enteredPinKeyLength);
    }


    public void clearEnteredPin() {
        int enteredPinKeyLength = enteredPin.length();
        if (enteredPinKeyLength != 0) enteredPin.delete(0, enteredPinKeyLength);
//        pinBarRefresh(0);
    }


    private void pinBarRefresh(int enteredPinKeyLength) {
        int[] pinKeyBar = {R.id.padDigit1, R.id.padDigit2, R.id.padDigit3, R.id.padDigit4};
        for (int i = 0; i < MAX_PIN_DIGITS; i++) {
            if (i < enteredPinKeyLength)
                fragmentView.findViewById(pinKeyBar[i]).setForegroundTintList(ColorStateList
                        .valueOf(ContextCompat.getColor(getActivity(), R.color.colorPinDigitYellow)));
            else
                fragmentView.findViewById(pinKeyBar[i]).setForegroundTintList(ColorStateList
                        .valueOf(ContextCompat.getColor(getActivity(), R.color.colorPinDigitGray)));
        }
    }

    // Fires when a configuration change occurs and fragment needs to save state
    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("screenHeader", screenHeader);
        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
//            screenHeader = savedInstanceState.getString("screenHeader");
        }
        // Inflate the layout for this fragment


        fragmentView = inflater.inflate(R.layout.get_pin, container, false);
        initViews();

        ((TextView) fragmentView.findViewById(R.id.get_key_screen_header)).setText(screenHeader);

        pinBarRefresh(enteredPin.length());

        return fragmentView;

    }


    public void initViews() {
        int[] digitPinPads = new int[]{R.id.pad0, R.id.pad1, R.id.pad2, R.id.pad3, R.id.pad4,
                R.id.pad5, R.id.pad6, R.id.pad7, R.id.pad8, R.id.pad9};


        Button.OnClickListener padOnClickListener = new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.pad_bkspc) {
                    removeDigitFromPin();
                } else {
                    int i;
                    for (i = 0; i < digitPinPads.length; i++) {
                        if (v.getId() == digitPinPads[i]) break;
                    }
                    if (i == digitPinPads.length) return;
                    addDigitToPin((char) (i + 0x30));
                    if (enteredPin.length() == MAX_PIN_DIGITS && onPinEntered != null) {
                        onPinEntered.onPinEntered();
                    }
                }

            }
        };

        for (int digitPinPad : digitPinPads) {
            fragmentView.findViewById(digitPinPad).setOnClickListener(padOnClickListener);
        }
        fragmentView.findViewById(R.id.pad_bkspc).setOnClickListener(padOnClickListener);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public boolean hasPin() {
        return ThisApp.getPinStore().hasPin();
    }

    @Override
    public boolean checkPin(String pin) {
        return ThisApp.getPinStore().checkPin(pin);
    }

    @Override
    public void saveNew(String pin) {
        ThisApp.getPinStore().saveNew(pin);
    }

    @Override
    public void clearPin() {
        ThisApp.getPinStore().clearPin();
    }

    public interface OnPinEntered {
        void onPinEntered();
    }

    public void setScreenHeader(String screenHeader) {
        this.screenHeader = screenHeader;
    }
}


