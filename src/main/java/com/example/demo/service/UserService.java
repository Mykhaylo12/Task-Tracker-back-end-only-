package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import java.util.List;

public interface UserService {
    User register(User user);

    List<UserDto> getAll(int limit);

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);

    User update(Long id, User user);
    void deleteByEmail(String email);

}
