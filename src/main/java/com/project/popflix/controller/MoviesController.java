package com.project.popflix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.popflix.repository.AuthoritiesRepository;
import com.project.popflix.repository.UserRepository;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.people.PersonCast;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class MoviesController {

 @Autowired
 UserRepository userRepository;
 @Autowired
 AuthoritiesRepository authoritiesRepository;

 private Long getUserId() {
  SecurityContext context = SecurityContextHolder.getContext();
  Authentication authentication = context.getAuthentication();
  Long id = userRepository.findByUsername(authentication.getName()).getId();
  return id;
 }

 @GetMapping("/")
 @ResponseBody
 public List<PersonCast> getMovie() {
  TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
  MovieDb movie = movies.getMovie(550, "en", MovieMethod.credits);
  System.out.println(movie.toString());
  System.out.println(movie.getBudget());
  System.out.println(movie.getPosterPath());
  System.out.println(movie.getReleaseDate());
  System.out.println(movie.getCredits().getCast());
  return movie.getCredits().getCast();
 }
}
