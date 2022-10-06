package com.project.popflix.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
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

  private String getMovieVideoLink(int id) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb movie = movies.getMovie(id, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
    return movie.getVideos().get(0).getKey();
  }

  @GetMapping("/movies")
  // @ResponseBody
  public String getMovie(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb movie = movies.getMovie(286217, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
    List<MovieDb> top20 = movies.getPopularMovies("en", 1).getResults();
    List<Integer> top20id = top20.stream().map(x -> x.getId()).collect(Collectors.toList());

    List<MovieDb> top20Vid = new ArrayList<>();
    for (int i = 0; i < top20id.size(); i++) {
      top20Vid = top20id.stream()
          .map(x -> movies.getMovie(x, "en", MovieMethod.images, MovieMethod.videos))
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

    // System.out.println(videoNested);
    List<MovieDb> firstList = new ArrayList<>(nested.get(0));
    nested.remove(0);
    videoNested.remove(0);

    // for(int i = 0; i < nested.size(); i++) {
    // nested
    // }

    model.addAttribute("firstList", firstList);
    // return firstList.get(0);
    model.addAttribute("vidExt", videoNested);
    model.addAttribute("movies", nested);
    // System.out.println(nested.get(0).get(0).getVideos().get(0).getKey());
    // model.addAttribute("watch", movies);

    // System.out.println(firstList);
    // System.out.println(secondList);
    // System.out.println(nested);
    return "movies/homepage";
  }

  @GetMapping("/test")
  @ResponseBody
  public MovieDb getData() {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb movie = movies.getMovie(550, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
    return movie;
  }

  @GetMapping("/")
  public String getHomePageForSignedInUser(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb movie = movies.getMovie(286217, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
    
    // System.out.println(movie.getVideos().get(0).getKey());
    List<MovieDb> top20 = movies.getPopularMovies("en", 1).getResults();
    List<Integer> top20id = top20.stream().map(x -> x.getId()).collect(Collectors.toList());

    // System.out.println(top20id);

    List<MovieDb> top20Vid = new ArrayList<>();
    for (int i = 0; i < top20id.size(); i++) {
      top20Vid = top20id.stream()
          .map(x -> movies.getMovie(x, "en", MovieMethod.images, MovieMethod.videos))
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

    // System.out.println(videoNested);
    List<MovieDb> firstList = new ArrayList<>(nested.get(0));
    nested.remove(0);
    videoNested.remove(0);

    model.addAttribute("firstList", firstList);
    // return firstList.get(0);
    model.addAttribute("vidExt", videoNested);
    model.addAttribute("movies", nested);
    // System.out.println(nested.get(0).get(0).getVideos().get(0).getKey());
    // model.addAttribute("watch", movies);

    return "movies/signedHomePage";
  }

  @GetMapping("/mostPopular")
  public String getMostPopularMovies(Model model) {

    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    List<MovieDb> mostPopular = movies.getPopularMovies("en", 2).getResults();

    model.addAttribute("movies", mostPopular);
    return "pages/mostPopular";
  }

  @GetMapping("/topRatings")
  public String getTopRatingMovies(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    // MovieDb movie = movies.getMovie(550, "en", MovieMethod.credits,
    // MovieMethod.images, MovieMethod.videos);
    MovieResultsPage topRated = movies.getTopRatedMovies("en", 1);
    model.addAttribute("movies", topRated);
    return "pages/topRatings";
  }

  @GetMapping("/nowPlayingMovies")
  public String getTopPicksMovies(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieResultsPage nowPlayingMovies = movies.getNowPlayingMovies("en", 2, "");
    System.out.println(nowPlayingMovies.getResults());
    // System.out.println(nowPlayingMovies.getPage());
    model.addAttribute("movies", nowPlayingMovies);
    return "pages/nowPlayingMovies";
  }

  @GetMapping("/upcomingMovies")
  public String getUpcomingMovies(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieResultsPage upcomingMovies = movies.getUpcoming("en", 1, "");
    model.addAttribute("movies", upcomingMovies);
    return "pages/upcomingMovies";
  }

  @GetMapping("/newReleases")
  public String getNewReleases(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    // MovieResultsPage newReleases = movies.get
    // model.addAttribute("movies", newReleases);
    return "pages/newReleases";
  }

  // @GetMapping("/watchGuide")
  // blic String getWatchGuide(Model model) {
  // return "watchGuide";
  // }
}
