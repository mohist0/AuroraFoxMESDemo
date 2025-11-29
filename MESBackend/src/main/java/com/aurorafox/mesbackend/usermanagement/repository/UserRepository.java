package com.aurorafox.mesbackend.usermanagement.repository;

import com.aurorafox.mesbackend.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 根据用户名查找用户
    User findByUserName(String userName);
}
