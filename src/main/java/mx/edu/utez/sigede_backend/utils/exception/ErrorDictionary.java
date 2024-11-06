package mx.edu.utez.sigede_backend.utils.exception;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorDictionary {
    private static final Map<String, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put("user.email.notnull", "El email es obligatorio.");
        errorMessages.put("user.email.invalid", "El email no es válido.");
        errorMessages.put("user.password.notnull", "La contraseña es obligatoria.");
        errorMessages.put("user.email.exists", "El email no esta disponible.");
        errorMessages.put("user.not.found", "El usuario ingresado no existe.");
        errorMessages.put("user.password.incorrect", "Contraseña incorrecta.");
        errorMessages.put("verification.code.expired", "El código de verificación ha expirado.");
        errorMessages.put("user.password.same_as_old", "La nueva contraseña no debe ser silimar a las anterior.");
        errorMessages.put("email.send.error", "Error al enviar correo. Revisa tu conexión o si la dirección de correo es correcta.");
    }

    public String getErrorMessage(String errorCode) {
        return errorMessages.getOrDefault(errorCode, "Error desconocido.");
    }
}
