package com.paris13.pls.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paris13.pls.entities.User;

/**
 * Créée par NDIAYE Sega 2019.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
