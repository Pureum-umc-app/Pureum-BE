package com.umc.pureum.config;

import com.umc.pureum.global.config.JasyptConfig;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest extends JasyptConfig {

    @Test
    public void jasypt_encrypt_decrypt_test() {
        String plainText = "TEXT";

        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword("password");

        String encryptedText = jasypt.encrypt(plainText);
        String decryptedText = jasypt.decrypt(encryptedText);

        System.out.println(encryptedText);
        System.out.println(decryptedText);
        assertThat(plainText).isEqualTo(decryptedText);
    }
}