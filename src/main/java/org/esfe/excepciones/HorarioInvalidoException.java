package org.esfe.excepciones;

public class HorarioInvalidoException extends RuntimeException {
    public HorarioInvalidoException(String message) {
        super(message);
    }
}
