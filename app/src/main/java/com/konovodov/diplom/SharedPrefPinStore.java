package com.konovodov.diplom;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SharedPrefPinStore implements PinStore {


    private final SharedPreferences sharedPreferences;
    private static final byte[] cipherKey = "38ipJ1zWkgckJjel".getBytes();
    private Key aesKey;
    private Cipher cipher;
    private boolean encryptionEnabled;


    public SharedPrefPinStore(Context context) {
        sharedPreferences = context.getSharedPreferences("pinstore", Context.MODE_PRIVATE);

        try {
            aesKey = new SecretKeySpec(cipherKey, "AES");
            cipher = Cipher.getInstance("AES");
            encryptionEnabled = true;
        } catch (Exception e) {
            e.printStackTrace();
            encryptionEnabled = false;
        }

    }

    @Override
    public boolean hasPin() {
        if (sharedPreferences.contains("pin")) {
            sharedPreferences.getString("pin", "");
            //вставить попытку дешифровки?
            return true;
        }
        return false;
    }

    @Override
    public boolean checkPin(String pin) {
        if (pin == null) return false;
        if (sharedPreferences.contains("pin")) {
            String appPin = deCrypt(sharedPreferences.getString("pin", null));
            if (appPin == null) return false;
            return pin.equals(appPin);
        }
        return false;
    }

    @Override
    public void saveNew(String pin) {
        sharedPreferences.edit().putString("pin", encrypt(pin)).apply();
    }

    @Override
    public void clearPin() {
        sharedPreferences.edit().clear().apply();
    }

    private String encrypt(String pin) {
        //для версий API < 26 шифрование не делается из-за отстутствия поддержки Base64
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return pin;
        else {
            if (!encryptionEnabled) return pin;
            String encryptedPin;
            try {
                cipher.init(Cipher.ENCRYPT_MODE, aesKey);
                byte[] encryptedPinInBytes = cipher.doFinal(pin.getBytes());
                encryptedPin = Base64.getEncoder().encodeToString(encryptedPinInBytes);
            } catch (Exception e) {
                encryptedPin = pin;
                e.printStackTrace();
            }
            return encryptedPin;
        }
    }

    private String deCrypt(String encryptedPin) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O)
            return encryptedPin;
        else {
            if (!encryptionEnabled) return encryptedPin;
            String pin;
            try {
                cipher.init(Cipher.DECRYPT_MODE, aesKey);
                byte[] encryptedPinInBytes = Base64.getDecoder().decode(encryptedPin);
                byte[] pinInBytes = cipher.doFinal(encryptedPinInBytes);
                pin = new String(pinInBytes);
            } catch (Exception e) {
                pin = encryptedPin;
                e.printStackTrace();
            }

            return pin;
        }
    }
}
