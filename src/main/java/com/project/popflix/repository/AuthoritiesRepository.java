package com.project.popflix.repository;

import com.project.popflix.model.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<Authority, Long> {
}