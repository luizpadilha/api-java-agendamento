package com.apimybarber.domain.viewobject;

import com.apimybarber.domain.entity.UserRole;

public record RegisterVO(String login, String password, UserRole role) {
}
