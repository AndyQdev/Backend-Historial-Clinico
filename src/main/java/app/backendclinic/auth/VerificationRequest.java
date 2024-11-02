package app.backendclinic.auth;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String code;
}