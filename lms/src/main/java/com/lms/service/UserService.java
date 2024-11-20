package com.lms.service;


import com.lms.dto.LoggedInUserDTO;
import com.lms.dto.RegisterUserDTO;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.utility.Response;
import com.lms.utility.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    public ResponseEntity<Object> createUser(RegisterUserDTO registerUserDTO) throws Exception {
        try {
            Optional<User> existingUser = userRepository.findByEmail(registerUserDTO.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.unsuccessfulResponse("This Email already existed!", null));

            }

            registerUserDTO.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));


            User newUser = modelMapper.map(registerUserDTO, User.class);
            newUser.setRole(UserRole.ADMIN);
            newUser = userRepository.save(newUser);
            LoggedInUserDTO loggedInUserDTO = modelMapper.map(newUser, LoggedInUserDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(Response.successfulResponse("Account Created Successfully!", loggedInUserDTO));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userModel = userRepository.findByEmail(username);
        if (userModel.isEmpty()) {
            throw new UsernameNotFoundException("This Username[" + username + "] Not Found");
        }
        return userModel.get();
    }
}
