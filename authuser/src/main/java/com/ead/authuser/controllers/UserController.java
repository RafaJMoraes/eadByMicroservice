package com.ead.authuser.controllers;


import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ead.authuser.services.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

//    @GetMapping
//    public ResponseEntity<List<UserModel>> getAllUser(){
//        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
//                                                       @PageableDefault(page = 0, size = 10, sort = "userId",
//                                                       direction = Sort.Direction.ASC) Pageable pageable){
//        Page<UserModel> userModelPage = userService.findAll(pageable);
//        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
//    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsersSpec(SpecificationTemplate.UserSpec spec,
                                                       @PageableDefault(page = 0, size = 10, sort = "userId",
                                                               direction = Sort.Direction.ASC) Pageable pageable){
        Page<UserModel> userModelPage = userService.findAll(pageable, spec);
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID id){
        Optional<UserModel> userModel = userService.findById(id);
        if(!userModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            return  ResponseEntity.status(HttpStatus.OK).body(userModel.get());
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID id){
        Optional<UserModel> userModel = userService.findById(id);
        if(!userModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else {
            userService.delete(userModel);
            return  ResponseEntity.status(HttpStatus.OK).body("Us");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID id,
                                             @RequestBody @Validated (UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        Optional<UserModel> userModel = userService.findById(id);
        if(!userModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else {
           UserModel userModel1 = new UserModel();
           userModel1.setFullName(userDto.getFullName());
           userModel1.setPhoneNumber(userDto.getPhoneNumber());
           userModel1.setCpf(userDto.getCpf());
           userModel1.setLastUpdateDate(getLastUpdateDate());
           userService.save(userModel1);
           return ResponseEntity.status(HttpStatus.OK).body(userModel1);
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updateUserPassword(@PathVariable(value = "userId") UUID id,
                                             @RequestBody @Validated (UserDto.UserView.PasswordPut.class)
                                             @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<UserModel> userModel = userService.findById(id);
        if(!userModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else {
            UserModel userModel1 = userModel.get();
            userModel1.setPassword(userDto.getPassword());
            userModel1.setLastUpdateDate(getLastUpdateDate());
            userService.save(userModel1);
            return ResponseEntity.status(HttpStatus.OK).body("Password update successfully.");
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateUserImage(@PathVariable(value = "userId") UUID id,
                                                     @RequestBody @Validated (UserDto.UserView.ImagePut.class)
                                                     @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
        Optional<UserModel> userModel = userService.findById(id);
        if(!userModel.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } else {
            UserModel userModel1 = userModel.get();
            userModel1.setImageUrl(userDto.getImageUrl());
            userModel1.setLastUpdateDate(getLastUpdateDate());
            userService.save(userModel1);
            return ResponseEntity.status(HttpStatus.OK).body("Image update successfully.");
        }
    }

    private static LocalDateTime getLastUpdateDate() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }


}
