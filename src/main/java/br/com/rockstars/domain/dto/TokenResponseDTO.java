package br.com.rockstars.domain.dto;

public class TokenResponseDTO {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;

    public TokenResponseDTO() {
    }

    public TokenResponseDTO(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.expiresIn = expiresIn;
    }

    public static TokenResponseDTO of(String accessToken, Long expiresIn) {
        return new TokenResponseDTO(accessToken, expiresIn);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
