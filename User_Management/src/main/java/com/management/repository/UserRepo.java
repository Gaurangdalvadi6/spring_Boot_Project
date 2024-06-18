package com.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.entity.OurUsers;

@Repository
public interface UserRepo extends JpaRepository<OurUsers, Integer>{

	Optional<OurUsers> findByEmail(String email);
}
