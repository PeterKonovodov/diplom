package com.konovodov.diplom;

import android.content.Context;

public class SimpleKeystore implements Keystore {
    Context context;

    public SimpleKeystore(Context context) {
        this.context = context;
    }

    @Override
    public boolean hasPin() {
        return false;
    }

    @Override
    public boolean checkPin(String pin) {
        return false;
    }

    @Override
    public void saveNew(String pin) {

    }
}
