// user/internal/UserService.java
package com.ordermanagement.user.internal;

import com.ordermanagement.shared.BusinessException;
import com.ordermanagement.shared.NotFoundException;
import com.ordermanagement.user.UserApi;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class UserService implements UserApi {
    private final UserRepository users;

    UserService(UserRepository users) { this.users = users; }

    @Override
    @Transactional(readOnly = true)
    public void assertActive(UUID userId) {
        User u = get(userId);
        if (!u.isActive()) throw new BusinessException("User đã bị vô hiệu hóa");
    }

    @Transactional
    public User create(String email, String fullName) {
        if (users.existsByEmail(email)) throw new BusinessException("Email đã tồn tại");
        return users.save(new User(email, fullName));
    }

    @Transactional(readOnly = true)
    public User get(UUID id) {
        return users.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy user " + id));
    }
}