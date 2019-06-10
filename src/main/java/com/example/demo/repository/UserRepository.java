package com.example.demo.repository;

import com.example.demo.domain.User;

public interface UserRepository extends BaseRepository<User,Long>{
    User findByName(String name);
}