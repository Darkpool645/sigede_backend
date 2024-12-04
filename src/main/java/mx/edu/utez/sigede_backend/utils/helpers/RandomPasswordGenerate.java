package mx.edu.utez.sigede_backend.utils.helpers;

import java.security.SecureRandom;

public class RandomPasswordGenerate {
    public String generatedRandomPassword(){
        final String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int PASSWORD_LENGTH = 12;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(ABC.length());
            password.append(ABC.charAt(randomIndex));
        }
        return password.toString();
    }
}
