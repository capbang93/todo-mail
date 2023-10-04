package com.example.todospringapp.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigAESTest {

    @Test
    public void checkEncrypt(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setProvider(new BouncyCastleProvider());
        encryptor.setPoolSize(2);
        encryptor.setPassword("ghdfewh2r384rhgelfw98wefkjewb23f");
        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");

        String plainText = "G5H9MNDUTRP7"; // 암호화 할 내용
        String encryptedText = encryptor.encrypt(plainText); // 암호화
        String decryptedText = encryptor.decrypt(encryptedText); // 복호화
        System.out.println("Enc:"+encryptedText+", Dec:"+decryptedText);
    }
}