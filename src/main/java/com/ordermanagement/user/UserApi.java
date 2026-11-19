// user/UserApi.java
package com.ordermanagement.user;

import java.util.UUID;

public interface UserApi {
    void assertActive(UUID userId);
}