

package com.project.popflix.controller;

import com.project.popflix.model.Authority;
import com.project.popflix.model.User;
import com.project.popflix.repository.AuthoritiesRepository;
import com.project.popflix.repository.UserRepository;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UsersController {

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

 PasswordEncoder passwordEncoder;

 @GetMapping("/users/new")
 public String signup(Model model) {
  model.addAttribute("user", new User());
  return "users/new"; // static html file
 }

 @PostMapping("/users")
 public RedirectView signup(@ModelAttribute User user) throws Exception {

  if (userRepository.existsByUsername(user.getUsername())) {
   // return ("error/wrong");
   return new RedirectView("users/new?retry");
  } else {
   passwordEncoder = new BCryptPasswordEncoder();
   String encodedPassword = passwordEncoder.encode(user.getPassword());
   user.setPassword(encodedPassword);

   userRepository.save(user);
   Authority authority = new Authority(user.getUsername(), user.getEmail(), "ROLE_USER");
   authoritiesRepository.save(authority);

   return new RedirectView("/login");
  }
 }

}
