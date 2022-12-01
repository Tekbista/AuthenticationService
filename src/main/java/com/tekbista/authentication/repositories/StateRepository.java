package com.tekbista.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tekbista.authentication.entities.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long>{

	
}
