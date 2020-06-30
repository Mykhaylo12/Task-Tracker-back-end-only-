package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        User registeredUser = userRepository.save(user);
        log.info("user: {} successfully registered/updated ", registeredUser);
        return registeredUser;
    }

    @Override
    public List<UserDto> getAll(int page) {
        Pageable pageWithTenElements = PageRequest.of(page, 10);
        Page<User> pages = userRepository.findAll(pageWithTenElements);
        List<User> content = pages.getContent();
        return content.stream().map(UserDto::toUserDto).collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        log.info("In findByEmail found user: {} ,by email {} ", user, email);
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        log.info("In findByEmail found user {} with id {}", user, id);
        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("In delete, user with id {} was deleted", id);
    }

    @Override
    public User update(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            return null;
        }
        userRepository.deleteById(userToUpdate.getId());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setPassword(user.getFirstName());
        return register(userToUpdate);
    }

    @Override
    public void deleteByEmail(String email) {
        userRepository.deleteUserByEmail(email);
    }
}
