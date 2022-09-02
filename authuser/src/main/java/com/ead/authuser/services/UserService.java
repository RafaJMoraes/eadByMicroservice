package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findById(UUID id);

    void delete(Optional<UserModel> userModel);

    void save(UserModel userModel);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    Page<UserModel> findAll(Pageable pageable);

    Page<UserModel> findAll(Pageable pageable, Specification<UserModel> spec);
}
