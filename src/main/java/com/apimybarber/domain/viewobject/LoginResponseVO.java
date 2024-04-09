package com.apimybarber.domain.viewobject;

public record LoginResponseVO(String token, String user, String userId, int expiresIn) {
}
