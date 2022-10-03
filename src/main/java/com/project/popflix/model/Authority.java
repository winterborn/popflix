package com.project.popflix.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "AUTHORITIES")
public class Authority {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String username;
 private String email;
 private String authority;

 public Authority() {
 }

 public Authority(String username, String email, String authority) {
  this.username = username;
  this.email = email;
  this.authority = authority;
 }
}
