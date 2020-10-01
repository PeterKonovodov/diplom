package com.konovodov.diplom;

public interface PinStore {
    boolean hasPin();
    boolean checkPin(String pin);
    void saveNew(String pin);
    void clearPin();
}