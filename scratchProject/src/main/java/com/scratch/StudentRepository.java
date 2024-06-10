package com.scratch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

	List<Student> findAllByFirstNameContaining(String firstname);
	
}
