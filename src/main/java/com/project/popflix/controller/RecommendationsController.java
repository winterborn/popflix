package com.project.popflix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video.Results;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import com.project.popflix.model.FormObj;
import com.project.popflix.model.User;
import com.project.popflix.model.Watchlist;
import com.project.popflix.repository.UserRepository;
import com.project.popflix.repository.WatchlistRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

@Controller
public class RecommendationsController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  WatchlistRepository watchlistRepository;

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

  @GetMapping("/recommendation")
  public String results(Model model, @ModelAttribute("formObj") FormObj formObj, BindingResult result,
      Exception Exception) throws Exception {

    TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();
    Iterable<Watchlist> watchlist = watchlistRepository.findByUserid(getUserId());

    List<Watchlist> ids = new ArrayList<>();

    watchlist.forEach(ids::add);
    Random rand = new Random();
    if (!ids.isEmpty() && ids.size() >= 1) { // 2 movies
      Integer r = rand.nextInt(ids.size()); // 0 - 1
      Integer id = ids.get(r).getMovieid();

      String movie = movies.getMovie(id, "en-US").getTitle();
      MovieResultsPage results = movies.getRecommendedMovies(id, "en-US", 1);


      List<MovieDb> list = new ArrayList<>();
      results.forEach(list::add);

      list = this.getMoviesWithVideo(list, movies);
      // List<MovieDb> movieList = ids.stream().map(
      // w -> movies.getMovie(w.getMovieid(), "en", MovieMethod.images,
      // MovieMethod.videos))
      // .collect(Collectors.toList());
      System.out.println(list);

      model.addAttribute("movies", list);

      model.addAttribute("searchedMovie", movie);
    }

    // model.addAttribute("searchedMovie", formObj.getSearch());
    model.addAttribute("formObj", new FormObj());

    return "pages/recommendation";
  }
}
