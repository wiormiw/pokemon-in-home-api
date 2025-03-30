package com.wiormiw.pokemon_in_home.repository;

import com.wiormiw.pokemon_in_home.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Role.RoleType name);
}
