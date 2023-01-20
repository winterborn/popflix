package com.project.popflix.repository;

import com.project.popflix.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
 boolean existsByUsername(String username);

 User findByUsername(String username);

}
