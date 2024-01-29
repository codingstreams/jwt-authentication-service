package io.github.codingstreams.jwtauthenticationservice.controller;


public record AuthResponseDto(String token, AuthStatus authStatus) {
}
