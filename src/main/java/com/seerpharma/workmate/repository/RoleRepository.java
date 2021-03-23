package com.seerpharma.workmate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seerpharma.workmate.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	@Query(nativeQuery=true, value = "select * from role where name =:name and account_id =:accountId)")
	Optional<Role> findByNameAndAccountId(String name, Long accountId);
	
	@Query(nativeQuery=true, value = "select * from role where account_id =:accountId)")
	Optional<List<Role>> findByAccountId(Long accountId);
}