package mx.edu.utez.sigede_backend.utils.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ErrorDictionary {
    private static final Map<String, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put("user.email.notnull", "El email es obligatorio.");
        errorMessages.put("user.email.invalid", "El email no es válido.");
        errorMessages.put("user.name.required", "Nombre requerido.");
        errorMessages.put("user.password.notnull", "La contraseña es obligatoria.");
        errorMessages.put("user.email.exists", "El email no esta disponible.");
        errorMessages.put("user.not.found", "El usuario ingresado no existe.");
        errorMessages.put("user.password.incorrect", "Contraseña incorrecta.");
        errorMessages.put("user.id.required", "Usuario requerido.");
        errorMessages.put("verification.code.expired", "El código de verificación ha expirado.");
        errorMessages.put("user.password.same_as_old", "La nueva contraseña no debe ser silimar a las anterior.");
        errorMessages.put("email.send.error", "Error al enviar correo. Revisa tu conexión o si la dirección de correo es correcta.");
        errorMessages.put("field.not.null", "El campo es obligatorio.");
        errorMessages.put("institution.name.error", "Ya existe una institucion registrada con ese nombre.");
        errorMessages.put("institution.id.notnull", "El Id de la institución es obligatorio.");
        errorMessages.put("capturer.email.error", "Ya existe una cuenta registrada con ese correo..");
        errorMessages.put("capturer.name.notnull", "El nombre no puede estar vacío.");
        errorMessages.put("capturer.password.notnull", "La contraseña es obligatoria.");
        errorMessages.put("admin.email.error", "Ya existe una cuenta registrada con ese correo.");
        errorMessages.put("rol.notfound", "Rol no encontrado.");
        errorMessages.put("rol.required", "Rol requerido.");
        errorMessages.put("status.notfound", "Estado no encontrado.");
        errorMessages.put("institution.notfound", "Institución no encontrada.");
        errorMessages.put("status.required", "Estado requerido.");
        errorMessages.put("status.same", "Estado sin cambios.");

    }

    public String getErrorMessage(String errorCode) {
        return errorMessages.getOrDefault(errorCode, "Error desconocido.");
    }
}
