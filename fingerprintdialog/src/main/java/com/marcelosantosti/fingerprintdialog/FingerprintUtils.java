package com.marcelosantosti.fingerprintdialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.jgabrielfreitas.datacontroller.DataController;

import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Created by mlsantos on 12/05/2016.
 */
public class FingerprintUtils {

    private static final String FINGERPRINT_KEY = "fingerprint";
    private static final String FINGERPRINT_PUBLIC_KEY = "fingerprintPublicKey";

    public static byte[] getSignBytes(Signature signature) throws SignatureException {

        Fingerprint fingerprint = new Fingerprint();
        signature.update(fingerprint.toByteArray());

        byte[] signatureBytes = signature.sign();

        return signatureBytes;
    }

    public static void saveFingerprintPublicKey(PublicKey signature, Context context) throws SignatureException {

        String encodedBytes = Base64.encodeToString(signature.getEncoded(), Base64.DEFAULT);

        new DataController(context).writeData(FINGERPRINT_PUBLIC_KEY, encodedBytes);
    }

    public static void saveFingerprintPublicKey(Signature signature, Context context) throws SignatureException {

        byte[] getSignBytes = getSignBytes(signature);

        String encodedBytes = Base64.encodeToString(getSignBytes, Base64.DEFAULT);

        new DataController(context).writeData(FINGERPRINT_KEY, encodedBytes);
    }

    public static boolean validateFingerprintPublicKey(Signature signature, Context context) throws SignatureException {

        byte[] getSignBytes = getSignBytes(signature);

        String encodedBytes = Base64.encodeToString(getSignBytes, Base64.DEFAULT);
        String savedSignature = getFingerprintPublicKey(context);

        return encodedBytes.equals(savedSignature);
    }

    public static boolean validateFingerprintPublicKey(PublicKey publicKey, Context context) throws SignatureException {

        byte[] getSignBytes = publicKey.getEncoded();

        String encodedBytes = Base64.encodeToString(getSignBytes, Base64.DEFAULT);
        String savedSignature = getFingerprintPublicKey(context);

        return encodedBytes.equals(savedSignature);
    }

    public static String getFingerprintPublicKey(Context context) throws SignatureException {

        return new DataController(context).readStringData(FINGERPRINT_PUBLIC_KEY);
    }
}
