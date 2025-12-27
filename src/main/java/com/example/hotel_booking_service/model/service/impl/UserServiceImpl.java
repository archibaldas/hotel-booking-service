package com.example.hotel_booking_service.model.service.impl;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.exception.NotChangeDataException;
import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.model.repository.UserRepository;
import com.example.hotel_booking_service.model.service.UserService;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;
import com.example.hotel_booking_service.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserById(Long id) {
        return userMapper.toResponseDto(findById(id));
    }

    @Override
    @Transactional
    public UserResponseDto createNewUser(UserRequestDto requestDto, RoleType roleType) {
        if(isRegisteredUser(requestDto.getUsername(), requestDto.getEmail())){
            throw new NotChangeDataException("Пользователь зарегистрирован с таким именем или электронным адресом");
        }
        User user = userMapper.toEntity(requestDto);
        user.setRoleType(roleType);
        user = userRepository.save(user);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NoFoundEntityException("Пользователь с именем: " + username + " не найден"));
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = findByUsername(username);
        UserResponseDto responseDto = userMapper.toResponseDto(user);
        responseDto.setRoleType(user.getRoleType().name());
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(u -> {
                    UserResponseDto responseDto = userMapper.toResponseDto(u);
                    responseDto.setRoleType(u.getRoleType().name());
                    return responseDto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NoFoundEntityException("Пользователь с Id: " + id + " не найден"));
    }

    @Override
    @Transactional
    public UserResponseDto update(Long id, UserRequestDto requestDto) {
        User updatedUser = findById(id);
        if(isRegisteredUser(requestDto.getUsername(), requestDto.getEmail())){
            throw new NotChangeDataException("Пользователь зарегистрирован с таким именем или электронным адресом");
        }
        updatedUser = userRepository.save(userMapper.updateEntityFromDto(requestDto, updatedUser));
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        User deletedUser = findById(id);
        userRepository.delete(deletedUser);
    }

    private boolean isRegisteredUser(String username, String email){
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    @Override
    public UserResponseDto create(UserRequestDto request) {
        return null;
    }
}
