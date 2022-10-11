package com.project.popflix.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import org.springframework.security.core.Authentication;

import com.project.popflix.model.FormObj;
import com.project.popflix.model.Watchlist;
import com.project.popflix.repository.UserRepository;
import com.project.popflix.repository.WatchlistRepository;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;

@Controller
public class WatchlistController {

    @Autowired
    WatchlistRepository watchlistRepository;
    @Autowired
    UserRepository userRepository;

    private Long getUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Long id = userRepository.findByUsername(authentication.getName()).getId();
        return id;
    }

    @PostMapping("/watchlist/new")
    public RedirectView addToWatchlist(@RequestParam("movieid") Integer movieid, @ModelAttribute Watchlist watchlist) {
        // watchlist/new?movieid=
        watchlist.setUserid(this.getUserId());
        watchlist.setMovieid(movieid);

        watchlistRepository.save(watchlist);

        return new RedirectView("/watchlist");
    }

    @GetMapping("/watchlist")
    public String watchlist(Model model) {

        TmdbMovies movies = new TmdbApi("d84f9365179dc98dc69ab22833381835").getMovies();

        Iterable<Watchlist> watchlist = watchlistRepository.findByUserid(getUserId());
        List<Integer> movieId = new ArrayList<>();
        for (Watchlist movie : watchlist) {
            movieId.add(movie.getMovieid());
        }

        List<MovieDb> moviesWatchlist = movieId
                .stream()
                .map(x -> movies
                        .getMovie(x, "en-US", MovieMethod.images, MovieMethod.videos))
                .collect(Collectors.toList());
        // System.out.println(moviesWatchlist);
        model.addAttribute("watchlist", moviesWatchlist);
        model.addAttribute("formObj", new FormObj());

        return "watchlist/user";
    }

    @RequestMapping("/watchlist/delete")
    @ResponseBody
    public RedirectView removeWatchlist(Model model, @RequestParam("movieid") Integer movieid) {
        Watchlist movie = watchlistRepository.findByUseridAndMovieid(getUserId(), movieid);
        watchlistRepository.deleteById(movie.getId());

        return new RedirectView("/watchlist");

    }
}
