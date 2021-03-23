package com.seerpharma.workmate.service;

import java.util.List;
import java.util.Optional;

import com.seerpharma.workmate.model.Role;

public interface RoleService {
	
	Optional<List<Role>> findByAccountId(Long id);
	
	Optional<Role> findByNameAndAccountId(String name, Long accountId);
	
	Role save(Role role);
	
	
}
