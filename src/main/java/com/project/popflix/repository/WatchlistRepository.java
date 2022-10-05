package com.project.popflix.repository;


import org.springframework.data.repository.CrudRepository;

import com.project.popflix.model.Watchlist;

public interface WatchlistRepository extends CrudRepository<Watchlist, Long> {

    Iterable<Watchlist> findByUserid(Long userid);

}