package com.project.popflix.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.project.popflix.model.FormObj;
import com.project.popflix.repository.AuthoritiesRepository;
import com.project.popflix.repository.UserRepository;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video.Results;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    MovieDb movie = movies.getMovie(id, "en-US", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
    return movie.getVideos().get(0).getKey();
  }

  private List<MovieDb> getMoviesWithVideo(List<MovieDb> initList, TmdbMovies movies) {
    List<MovieDb> withVideo = new ArrayList<>();
    List<Integer> movieids = initList.stream().map(x -> x.getId()).collect(Collectors.toList());
    for (int i = 0; i < movieids.size(); i++) {
      withVideo = movieids.stream()
          .map(x -> movies.getMovie(x, "en-US", MovieMethod.images, MovieMethod.videos))
          .collect(Collectors.toList());
    }

    for (MovieDb movie : withVideo) {

      Results empty = new Results();
      if (movie.getVideos() == null) {
        movie.setVideos(empty);
        movie.getVideos().get(0).setKey("dasdasa");
      }
    }
    return withVideo;
  }

  @GetMapping("/movies")
  // @ResponseBody
  public String getMovie(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb movie = movies.getMovie(286217, "en-US", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);
    List<MovieDb> top20 = movies.getPopularMovies("en-US", 1).getResults();
    List<Integer> top20id = top20.stream().map(x -> x.getId()).collect(Collectors.toList());

    TmdbMovies movie_test = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb fightClub = movie_test.getMovie(550, "en-US", MovieMethod.credits);

    // for (int i = 0; i < fightClub.getCrew().size(); i++) {
    // if (fightClub.getCrew().get(i).getJob().equals("Director")) {
    // System.out.println(fightClub.getCrew().get(i).getName());
    // }
    // }

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

  // @GetMapping("/test")
  // @ResponseBody
  // public MovieDb getData() {
  // TmdbMovies movies = new
  // TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
  // MovieDb movie = movies.getMovie(550, "en-US", MovieMethod.credits,
  // MovieMethod.images, MovieMethod.videos);
  // return movie;
  // }

  @GetMapping("/")
  public String getHomePageForSignedInUser(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieDb movie = movies.getMovie(286217, "en-US", MovieMethod.credits, MovieMethod.images, MovieMethod.videos);

    // System.out.println(movie.getVideos().get(0).getKey());
    List<MovieDb> top20 = movies.getPopularMovies("en-US", 1).getResults();
    List<Integer> top20id = top20.stream().map(x -> x.getId()).collect(Collectors.toList());

    // **********************
    List<MovieDb> newMovies = movies.getRecommendedMovies(550, "en-US", 1).getResults();
    List<Integer> newMoviesIds = newMovies.stream().map(x -> x.getId()).collect(Collectors.toList());
    // **********************

    // System.out.println(top20id);

    List<MovieDb> top20Vid = new ArrayList<>();
    List<MovieDb> newMoviesVid = new ArrayList<>();
    for (int i = 0; i < top20id.size(); i++) {
      top20Vid = top20id.stream()
          .map(x -> movies.getMovie(x, "en-US", MovieMethod.images, MovieMethod.videos))
          .collect(Collectors.toList());
    }

    for (int i = 0; i < newMoviesIds.size(); i++) {
      newMoviesVid = newMoviesIds.stream()
          .map(x -> movies.getMovie(x, "en-US", MovieMethod.images, MovieMethod.videos))
          .collect(Collectors.toList());
    }

    List<List<MovieDb>> nested = new ArrayList<>();
    List<List<String>> videoNested = new ArrayList<>();

    // ************************************
    List<List<MovieDb>> newMoviesNested = new ArrayList<>();
    List<List<String>> newMoviesVideoNested = new ArrayList<>();

    // ************************************

    for (int i = 0; i < 20; i += 5) { // 1
      List<MovieDb> list = new ArrayList<>();
      List<String> vidList = new ArrayList<>();

      List<MovieDb> newMoviesList = new ArrayList<>();
      List<String> newMoviesVidList = new ArrayList<>();

      for (int j = i; j < i + 5; j++) { // 3
        list.add(top20Vid.get(j));
        newMoviesList.add(newMoviesVid.get(j));
        if (top20Vid.get(j).getVideos() == null || top20Vid.get(j).getVideos().size() == 0) {
          vidList.add("");
        } else {
          vidList.add(top20Vid.get(j).getVideos().get(0).getKey());
        }

        if (newMoviesVid.get(j).getVideos() == null || newMoviesVid.get(j).getVideos().size() == 0) {
          newMoviesVidList.add("");
        } else {
          newMoviesVidList.add(newMoviesVid.get(j).getVideos().get(0).getKey());
        }
      }
      videoNested.add(vidList);
      newMoviesVideoNested.add(newMoviesVidList);
      nested.add(list);
      newMoviesNested.add(newMoviesList);
    }

    // System.out.println(videoNested);
    List<MovieDb> firstList = new ArrayList<>(nested.get(0));
    nested.remove(0);
    videoNested.remove(0);

    // ************************************
    List<MovieDb> newMoviesFirstList = new ArrayList<>(newMoviesNested.get(0));
    newMoviesNested.remove(0);
    newMoviesVideoNested.remove(0);

    // ************************************
    model.addAttribute("newMoviesFirstList", newMoviesFirstList);
    model.addAttribute("newMoviesNested", newMoviesNested);
    model.addAttribute("formObj", new FormObj());

    // System.out.println("NEW MOVIES HERE:");
    // System.out.println(newMoviesFirstList);
    // System.out.println(newMoviesNested);
    // ************************************

    model.addAttribute("firstList", firstList);
    // return firstList.get(0);
    model.addAttribute("vidExt", videoNested);
    model.addAttribute("movies", nested);
    // model.addAttribute("watch", movies);

    return "movies/signedHomePage";
  }

  @GetMapping("/mostPopular")
  public String getMostPopularMovies(Model model) {

    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    List<MovieDb> mostPopular = movies.getPopularMovies("en-US", 1).getResults();
    mostPopular = this.getMoviesWithVideo(mostPopular, movies);

    model.addAttribute("movies", mostPopular);
    model.addAttribute("formObj", new FormObj());

    return "pages/mostPopular";
  }

  @GetMapping("/topRatings")
  public String getTopRatingMovies(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    // MovieDb movie = movies.getMovie(550, "en", MovieMethod.credits,
    // MovieMethod.images, MovieMethod.videos);
    MovieResultsPage topRated = movies.getTopRatedMovies("en-US", 1);
    List<MovieDb> list = new ArrayList<>();
    topRated.forEach(list::add);
    list = this.getMoviesWithVideo(list, movies);

    // this.getMoviesWithVideo(topRated, movies);
    model.addAttribute("movies", list);
    model.addAttribute("formObj", new FormObj());

    return "pages/topRatings";
  }

  @GetMapping("/nowPlayingMovies")
  public String getTopPicksMovies(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieResultsPage nowPlayingMovies = movies.getNowPlayingMovies("en-US", 1, "");

    List<MovieDb> list = new ArrayList<>();
    nowPlayingMovies.forEach(list::add);
    list = this.getMoviesWithVideo(list, movies);

    for (MovieDb movie : list) {
      System.out.println(movie.getVideos());
    }

    model.addAttribute("movies", list);
    model.addAttribute("formObj", new FormObj());

    return "pages/nowPlayingMovies";
  }

  @GetMapping("/upcomingMovies")
  public String getUpcomingMovies(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    MovieResultsPage upcomingMovies = movies.getUpcoming("en-US", 1, "");

    List<MovieDb> list = new ArrayList<>();
    upcomingMovies.forEach(list::add);
    list = this.getMoviesWithVideo(list, movies);

    model.addAttribute("movies", list);
    model.addAttribute("formObj", new FormObj());

    return "pages/upcomingMovies";
  }

  @GetMapping("/newReleases")
  public String getNewReleases(Model model) {
    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    // MovieResultsPage newReleases = movies.get
    // model.addAttribute("movies", newReleases);
    model.addAttribute("formObj", new FormObj());

    return "pages/newReleases";
  }

  // ******************
  @RequestMapping("/results")
  public String results(Model model, @ModelAttribute("formObj") FormObj formObj, BindingResult result) {
    // String searchedItem = "superman";
    TmdbSearch search = new TmdbApi("d84f9365179dc98dc69ab22833381835").getSearch();
    List<MovieDb> results = search.searchMovie(formObj.getSearch(), null, null, false, null).getResults();

    model.addAttribute("movies", results);
    model.addAttribute("searchedMovie", formObj.getSearch());
    model.addAttribute("formObj", new FormObj());

    return "pages/results";
  }

  // @PostMapping("/results")
  // public RedirectView getInput(@ModelAttribute MovieDb form) {

  // return new RedirectView("/results");
  // }
}
