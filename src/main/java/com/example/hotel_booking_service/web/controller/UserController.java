package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.model.service.UserService;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserByUsername(@RequestParam String username){
        return userService.getUserByUsername(username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createNewUser(@RequestBody UserRequestDto userRequestDto,
                                         @RequestParam RoleType roleType){
        return userService.createNewUser(userRequestDto, roleType);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody UserRequestDto userRequestDto){
        return userService.update(id, userRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteById(id);
    }
}
