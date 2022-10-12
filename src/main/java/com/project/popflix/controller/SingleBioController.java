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
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi.MediaType;
import info.movito.themoviedbapi.model.Video.Results;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.people.PersonPeople;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class SingleBioController {
    @RequestMapping("/bio")
    public String bioDetails(@RequestParam("personid") Integer personid, Model model) {
        TmdbPeople people = new TmdbApi("d84f9365179dc98dc69ab22833381835").getPeople();
        PersonPeople person = people.getPersonInfo(personid);
        String name = person.getName();
        String bio = person.getBiography();
        String department = person.getKnownForDepartment();
        // String unknown = person.handleUnknown(bio, job);

        float popularity = person.getPopularity();
        String birthday = person.getBirthday();
        String deathdate = person.getDeathday();
        String personalPage = person.getHomepage();
        List<Artwork> person_img = people.getPersonImages(personid);
        String img = person_img.get(0).getFilePath();
        MediaType media = person.getMediaType();
        String character = person.getCharacter();
        int cast = person.getCastId();
        String birthPlace = person.getBirthplace();
        String profilePath = person.getProfilePath();

        model.addAttribute("img", img);
        model.addAttribute("character", character);
        model.addAttribute("media", media);
        model.addAttribute("name", name);
        model.addAttribute("department", department);
        model.addAttribute("bio", bio);
        model.addAttribute("popularity", popularity);
        model.addAttribute("birthday", birthday);
        model.addAttribute("death", deathdate);
        model.addAttribute("personalPage", personalPage);
        model.addAttribute("cast", cast);
        model.addAttribute("pob", birthPlace);
        model.addAttribute("profile", profilePath);
        model.addAttribute("formObj", new FormObj());

        return "pages/bio_page";
    }
}
