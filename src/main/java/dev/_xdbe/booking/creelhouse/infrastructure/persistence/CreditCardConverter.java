package dev._xdbe.booking.creelhouse.infrastructure.persistence;


import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

import dev._xdbe.booking.creelhouse.infrastructure.persistence.CryptographyHelper;


@Converter
public class CreditCardConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return CryptographyHelper.encryptData(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        String pan = CryptographyHelper.decryptData(dbData);
        String maskedPanString = panMasking(pan);
        return maskedPanString;
    }

    private String panMasking(String pan) {
        if (pan == null || pan.length() < 8) {
            return pan;
        }
        String firstFour = pan.substring(0, 4);
        String lastFour = pan.substring(pan.length() - 4);
        String masked = "*".repeat(pan.length() - 8);
        return firstFour + masked + lastFour;
    }

    
}