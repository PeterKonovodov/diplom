package com.konovodov.diplom;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

public class PinManager implements PinStore {

    private final StringBuilder enteredPin;
    private final Activity activity;
    private final int MAX_PIN_DIGITS = 4;
    private WhatToDoWithPin whatToDoWithPin = null; //это объект интерфейса, выполняющий действия
    // при наборе полного пинкода. В некоторых случаях это проверка,
    // в некоторых - получение нового пина, в других - подтверждение пинкода для его сброса


    public PinManager(Activity activity) {
        this.activity = activity;
        enteredPin = new StringBuilder();
        InitViews();
    }

    public void InitViews() {
        int[] digitPinPads = new int[] {R.id.pad0, R.id.pad1, R.id.pad2, R.id.pad3, R.id.pad4,
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
                    if (enteredPin.length() == MAX_PIN_DIGITS && whatToDoWithPin != null) {
                        whatToDoWithPin.doThis();
                    }
                }

            }
        };

        for (int digitPinPad : digitPinPads) {
            activity.findViewById(digitPinPad).setOnClickListener(padOnClickListener);
        }
        activity.findViewById(R.id.pad_bkspc).setOnClickListener(padOnClickListener);
    }

    //этот метод устанавливает требуемый коллбэк при завершении ввода пинкода.
    //а сам коллбэк делается из интерфейса WhatToDoWithPin
    public void SetWhatToDoWithPin(WhatToDoWithPin whatToDoWithPin) {
        this.whatToDoWithPin = whatToDoWithPin;
    }


    private void addDigitToPin(char symbol) {
        if (enteredPin.length() < MAX_PIN_DIGITS) enteredPin.append(symbol);
        pinBarRefresh(enteredPin.length());
    }

    public void clearEnteredPin() {
        int enteredPinKeyLength = enteredPin.length();
        if (enteredPinKeyLength != 0) enteredPin.delete(0, enteredPinKeyLength);
        pinBarRefresh(0);
    }

    private void removeDigitFromPin() {
        int enteredPinKeyLength = enteredPin.length();
        if (enteredPinKeyLength == 0) return;
        enteredPin.deleteCharAt(enteredPinKeyLength - 1);
        pinBarRefresh(--enteredPinKeyLength);
    }

    private void pinBarRefresh(int enteredPinKeyLength) {
        int[] pinKeyBar = {R.id.padDigit1, R.id.padDigit2, R.id.padDigit3, R.id.padDigit4};
        for (int i = 0; i < MAX_PIN_DIGITS; i++) {
            if (i < enteredPinKeyLength)
                activity.findViewById(pinKeyBar[i]).setForegroundTintList(ColorStateList
                        .valueOf(ContextCompat.getColor(activity, R.color.colorPinDigitYellow)));
            else
                activity.findViewById(pinKeyBar[i]).setForegroundTintList(ColorStateList
                        .valueOf(ContextCompat.getColor(activity, R.color.colorPinDigitGray)));
        }
    }


    public String getEnteredPin() {
        if (enteredPin.length() < MAX_PIN_DIGITS) return null;
        return enteredPin.toString();
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

    public interface WhatToDoWithPin {
        void doThis();
    }

}
