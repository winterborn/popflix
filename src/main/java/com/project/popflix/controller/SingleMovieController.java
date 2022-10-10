package com.project.popflix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;

@Controller
public class SingleMovieController {

 @RequestMapping("/movie")
 public String movieDetails(@RequestParam("movieid") Integer movieid, Model model) {
  TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
  MovieDb movie = movies.getMovie(movieid, "en-US", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
  List<MovieDb> top20 = movies.getRecommendedMovies(movieid, "en-US", 1).getResults();
  List<Integer> top20id = top20.stream().map(x -> x.getId()).collect(Collectors.toList());

  List<MovieDb> top20Vid = new ArrayList<>();
  for (int i = 0; i < top20id.size(); i++) {
   top20Vid = top20id.stream()
     .map(x -> movies.getMovie(x, "en-US", MovieMethod.images, MovieMethod.videos))
     .collect(Collectors.toList());
  }

  List<List<MovieDb>> nested = new ArrayList<>();
  List<List<String>> videoNested = new ArrayList<>();

  for (int i = 0; i < 20; i += 5) { // 1
   List<MovieDb> list = new ArrayList<>();
   List<String> vidList = new ArrayList<>();
   for (int j = i; j < i + 5; j++) { // 3
    list.add(top20Vid.get(j));
    if (top20Vid.get(j).getVideos() == null || top20Vid.get(j).getVideos().size() == 0) {
     vidList.add("");
    } else {
     vidList.add(top20Vid.get(j).getVideos().get(0).getKey());
    }
   }
   videoNested.add(vidList);
   nested.add(list);
  }

  List<MovieDb> firstList = new ArrayList<>(nested.get(0));
  nested.remove(0);
  videoNested.remove(0);

  model.addAttribute("firstList", firstList);
  model.addAttribute("vidExt", videoNested);
  model.addAttribute("movies", nested);
  model.addAttribute("movie", movie);

  return "movies/movieIndPage";
 }
}