package br.com.fiap.oficina.api.modules.identidade.api.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record AtualizarTokenRequest(
                @NotBlank(message = "O refresh token é obrigatório") @Schema(description = "Refresh Token", defaultValue = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPZmljaW5hQVBJIiwiYXVkIjoiT2ZpY2luYUFwcCIsImlhdCI6MTc0NDI3NDY1MSwiRXhwIjoxNzQ0MzYxMDUxfQ.K-qXk9d_Yn-wzH7rFvD4yB2Xz-wzH7rFvD4yB2Xz-wz") @JsonProperty("refresh_token") String refreshToken) {

}






