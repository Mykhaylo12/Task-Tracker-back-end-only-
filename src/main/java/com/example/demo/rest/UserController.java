package com.example.demo.rest;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody User requestUser) {
        User user;
        try {
            user = userService.register(requestUser);
        } catch (Exception e) {
            return new ResponseEntity(e.toString(), HttpStatus.BAD_REQUEST);
        }
        UserDto userDto = UserDto.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody User requestUser) {
        String token;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestUser.getEmail(),
                    requestUser.getPassword()));
            User user = userService.findByEmail(requestUser.getEmail());
            if (user == null) {
                return new ResponseEntity("Could't find user with email " +
                        requestUser.getEmail(), HttpStatus.BAD_REQUEST);
            }
            token = jwtTokenProvider.createToken(requestUser.getEmail(), user.getRoles());
        } catch (AuthenticationException e) {
            return new ResponseEntity("Invalid email or password", HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("email", requestUser.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity update(@PathVariable long id, @Valid @RequestBody User requestUser) {
        User userToUpdate = userService.update(id, requestUser);
        if (userToUpdate == null) {
            return new ResponseEntity("User with id " + id + " does not exist", HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(UserDto.toUserDto(userToUpdate));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity("User with id " + id + " does not exist", HttpStatus.BAD_REQUEST);
        }
        userService.delete(id);
        return ResponseEntity.ok(UserDto.toUserDto(user));
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserDto> get(@PathVariable long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity("User with id " + id + " does not exist", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(UserDto.toUserDto(user));
    }
}
