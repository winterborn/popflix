package com.project.popflix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.popflix.model.Watchlist;

public interface WatchlistRepository extends CrudRepository<Watchlist, Long> {

    Iterable<Watchlist> findByUserid(Long userid);

    Watchlist findByUseridAndMovieid(Long userid, Integer movieid);

    void deleteById(Long id);

    @Query(value = "SELECT * FROM watchlist WHERE userid = ?1", nativeQuery = true)
    Iterable<Watchlist> findAllMoviesByUserid(Long userid, Integer movieid); // needs to check for watchlist by userid
}