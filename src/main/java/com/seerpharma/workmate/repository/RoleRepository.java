package com.seerpharma.workmate.repository;

import java.util.Optional;

import com.seerpharma.workmate.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seerpharma.workmate.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(String name);
}