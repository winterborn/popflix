package com.project.popflix.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 // @ResponseBody
 public String getMovie(Model model) {
  TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
  MovieDb movie = movies.getMovie(550, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
  // System.out.println(movie.toString());
  // System.out.println(movie.getBudget());
  // System.out.println(movie.getReleaseDate());
  // System.out.println(movie.getCredits().getCast());
  // System.out.println(movie.getPosterPath());
  // System.out.println(movies.getPopularMovies("en", 1));
  List<MovieDb> top20 = movies.getPopularMovies("en", 1).getResults();
  List<List<MovieDb>> nested = new ArrayList<>();
  // System.out.println("---------------HERE-----------------");
  // System.out.println(top20.size());

  for (int i = 0; i < 20; i += 5) { // 1
   List<MovieDb> list = new ArrayList<>();
   for (int j = i; j < i + 5; j++) { // 3
    list.add(top20.get(j));
   }
   nested.add(list);
  }

  List<MovieDb> firstList = new ArrayList<>(nested.get(0));
  nested.remove(0);

  model.addAttribute("firstList", firstList);
  model.addAttribute("movies", nested);

  // System.out.println(firstList);
  // System.out.println(secondList);
  System.out.println(nested);
  return "movies/top20";
 }
}
