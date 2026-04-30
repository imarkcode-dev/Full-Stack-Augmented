package com.nexus.app.user.domain.projection;

import java.time.LocalDateTime;

public interface LoginProjection {
    Integer getUserId();
    String getName();
    LocalDateTime getLastLogin();
}
