package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.UserDTO;
import com.example.ShipManagementSystem.entity.UserEntity;
import com.example.ShipManagementSystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    final ModelMapper modelMapper;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }



    public List<UserEntity> getUsers(){
        return  userRepository.findAll();
    }

    public UserDTO createUser(UserDTO user){
        UserEntity userEntity = modelMapper.map(user,UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        return  modelMapper.map(userRepository.save(userEntity), UserDTO.class);

    }
}
