package com.project.capstone.Services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionAndDecryption {
    private String key="W0JAM2U3ODA1Mzg=";
    private String iv = "1234567812345678";

//    public SecretKey generateKey() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        SecureRandom secureRandom = new SecureRandom();
//        keyGenerator.init(256, secureRandom);
//        key = keyGenerator.generateKey();
//        Base64.Encoder encoder = Base64.getEncoder();
//        byte[] bytes = key.getEncoded();
//        String StringOfKey = encoder.encodeToString(bytes.toString().getBytes());
//        System.out.println(StringOfKey);
////        System.out.println(key.toString());
//        return key;
//    }

    public SecretKey secretKeyToByte(){
        byte[] decodeKey = Base64.getDecoder().decode(key);
        byte[] paddedKey = new byte[16];
        System.arraycopy(decodeKey,0,paddedKey,0,Math.min(decodeKey.length,paddedKey.length));
        SecretKey secretKey = new SecretKeySpec(paddedKey,"AES");
        return secretKey;
    }

    public String encrypt(String data) throws Exception {
        SecretKey secretKey = secretKeyToByte();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey,ivParameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    public String decrypt(String encryptedData) throws Exception {
        SecretKey secretKey = secretKeyToByte();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey,ivParameterSpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }
}