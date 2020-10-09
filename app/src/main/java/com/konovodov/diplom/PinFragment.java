package com.konovodov.diplom;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PinFragment extends Fragment implements PinStore {


    private View fragmentView;
    private int activityState;
    //enteredPin - переменная, заполняемая в листенере, т.е. в статическом контексте
    //поэтому тоже статическая, иначе наблюдаются баги
    private static StringBuilder enteredPin = new StringBuilder();
    private final int MAX_PIN_DIGITS = 4;
    private static OnPinEntered onPinEntered = null; //это ссылка на объект интерфейса, выполняющий действия
    // при наборе полного пинкода. В некоторых случаях это проверка,
    // в некоторых - получение нового пина, в других - подтверждение пинкода для его сброса
    // должна быть static, чтобы при пересоздании фрагмента колбэк не пропадал.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //enteredPin хранится в Bundle savedInstanceState
        if (savedInstanceState != null) {
            enteredPin = new StringBuilder(savedInstanceState.getString("enteredPin", ""));
        }
        //activityState хранится в Arguments, а это другой объект Bundle
        activityState = getArguments().getInt("activityState", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.pin_fragment, container, false);
        initViews();

        switch (activityState) {
            case NotesMainActivity.CHECK_PIN:
                ((TextView) fragmentView.findViewById(R.id.get_key_screen_header)).setText(getString(R.string.enter_pin));
                break;
            case NotesMainActivity.SET_NEW_PIN:
                ((TextView) fragmentView.findViewById(R.id.get_key_screen_header)).setText(getString(R.string.enter_new_pin));
                break;
            case NotesMainActivity.RESET_PIN:
                ((TextView) fragmentView.findViewById(R.id.get_key_screen_header)).setText(getString(R.string.pin_confirm_to_delete));
                break;
        }

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
    public void onSaveInstanceState(Bundle outState) {
        //сохранение enteredPin необходимо для сохранения вида экрана при повороте
        //т.е. сохранения уже введенных цифр
        outState.putString("enteredPin", enteredPin.toString());
        super.onSaveInstanceState(outState);
    }

    public static PinFragment getInstance(int activityState, OnPinEntered onPinEntered) {
        PinFragment fragment = new PinFragment();
        enteredPin = new StringBuilder("");
        fragment.setOnPinEntered(onPinEntered);
        Bundle args = new Bundle();
        args.putInt("activityState", activityState);
        fragment.setArguments(args);
        return fragment;
    }

    //этот метод устанавливает требуемый коллбэк при завершении ввода пинкода.
    //а сам коллбэк делается из интерфейса OnPinEntered
    public void setOnPinEntered(OnPinEntered onPinEntered) {
        this.onPinEntered = onPinEntered;
    }

    public String getEnteredPin() {
        if (enteredPin.length() < MAX_PIN_DIGITS) return "";
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

}


