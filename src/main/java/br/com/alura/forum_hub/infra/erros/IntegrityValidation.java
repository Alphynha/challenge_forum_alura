package br.com.alura.forum_hub.infra.erros;

import java.io.Serial;

public class IntegrityValidation extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public IntegrityValidation(String message) {
        super(message);
    }
}
