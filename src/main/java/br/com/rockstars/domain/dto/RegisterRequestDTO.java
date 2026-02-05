package br.com.rockstars.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    @NotBlank(message = "Username e obrigatorio")
    @Size(min = 3, max = 100, message = "Username deve ter entre 3 e 100 caracteres")
    private String username;

    @NotBlank(message = "Password e obrigatorio")
    @Size(min = 6, max = 100, message = "Password deve ter entre 6 e 100 caracteres")
    private String password;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
