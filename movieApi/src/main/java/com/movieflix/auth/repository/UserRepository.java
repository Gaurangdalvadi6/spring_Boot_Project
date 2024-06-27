package com.movieflix.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.movieflix.auth.entity.User;

import jakarta.transaction.Transactional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByEmail(String username);
	
	@Modifying
	@Transactional
	@Query("update User u set u.password =?2 where u.email =?1")
	void updatePassword(String email,String password);
}
