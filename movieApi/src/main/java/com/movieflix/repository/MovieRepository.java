package com.movieflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movieflix.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>{

	
}
