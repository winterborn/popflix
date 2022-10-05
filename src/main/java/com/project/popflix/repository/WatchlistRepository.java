package com.project.popflix.repository;

import org.springframework.data.repository.CrudRepository;

import com.project.popflix.model.Authority;

public interface WatchlistRepository extends CrudRepository<Authority, Long> {
}