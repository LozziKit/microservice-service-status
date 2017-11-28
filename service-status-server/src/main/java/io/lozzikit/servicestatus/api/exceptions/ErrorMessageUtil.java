package io.lozzikit.servicestatus.api.exceptions;

public class ErrorMessageUtil {

    static public String buildEntityNotFoundMessage(String entityName) {
        return entityName + " does not exist";
    }
}
