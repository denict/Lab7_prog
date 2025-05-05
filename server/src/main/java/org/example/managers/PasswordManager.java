package org.example.managers;


import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Менеджер для хеширования паролей.
 * Обеспечивает безопасное хеширование паролей с использованием алгоритма SHA-256.
 * Используется для защиты паролей пользователей при хранении в базе данных.
 */
public class PasswordManager {

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Преобразуем пароль в кодировке UTF-8 в хэш
            byte[] passwordBytes = password.getBytes("UTF-8");

            // Создаем хэш пароля
            BigInteger hash = new BigInteger(1, digest.digest(passwordBytes));
            return hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Не найден алгоритм при хешировании пароля", e); // для удобства использования
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Ошибка кодировки при хешировании пароля", e);
        }
    }
}
