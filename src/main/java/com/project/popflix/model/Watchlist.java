package com.project.popflix.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "WATCHLIST")
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userid;
    private Integer movieid;

    public Watchlist(Long userid, Integer movieid) {
        this.userid = userid;
        this.movieid = movieid;
    }

    public Watchlist(Long id, Long userid, Integer movieid) {
        this.movieid = movieid;
    }

    public Watchlist() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return this.userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getMovieid() {
        return this.movieid;
    }

    public void setMovieid(Integer movieid) {
        this.movieid = movieid;
    }
}
