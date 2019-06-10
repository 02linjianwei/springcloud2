package com.example.demo.service;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;

import java.util.Map;

public interface ILoginService {
    public User addUser(Map<String, Object> map);
    public Role addRole(Map<String, Object> map);
    public User findByName(String name);
}
