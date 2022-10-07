package com.project.popflix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;

@Controller
public class SingleMovieController {

 // @GetMapping("/movie")
 // public String getMovieDetails(Model model) {

 // return "movies/movieIndPage";
 // }

 @RequestMapping("/movie")
 // @ResponseBody
 public String movieDetails(@RequestParam("movieid") Integer movieid, Model model) {
  TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
  MovieDb movie = movies.getMovie(movieid, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);

  model.addAttribute("movie", movie);

  return "movies/movieIndPage";
 }
}
