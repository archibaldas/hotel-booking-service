package com.example.hotel_booking_service.model.service.impl;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.exception.NotAuthorizationException;
import com.example.hotel_booking_service.exception.NotChangeDataException;
import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.model.entity.UserRole;
import com.example.hotel_booking_service.model.repository.UserRepository;
import com.example.hotel_booking_service.model.service.UserService;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;
import com.example.hotel_booking_service.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new NotAuthorizationException("Пользователь не авторизован");
        }
        String username = auth.getName();
        return findByUsername(username);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = findById(id);
        UserResponseDto responseDto = userMapper.toResponseDto(user);
        responseDto.setUserRoles(user.getRoles().stream()
                .map(r -> r.getAuthority().name()).toList());
        return responseDto;
    }

    @Override
    @Transactional
    public UserResponseDto createNewUser(UserRequestDto requestDto, RoleType roleType) {
        if(isRegisteredUser(requestDto.getUsername(), requestDto.getEmail())){
            throw new NotChangeDataException("Пользователь зарегистрирован с таким именем или электронным адресом");
        }
        User user = userMapper.toEntity(requestDto);
        UserRole role = UserRole.from(roleType);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);
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
        responseDto.setUserRoles(user.getRoles().stream()
                .map(r -> r.getAuthority().name()).toList());
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(u -> {
                    UserResponseDto responseDto = userMapper.toResponseDto(u);
                    responseDto.setUserRoles(u.getRoles().stream()
                            .map(r -> r.getAuthority().name()).toList());
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
        if(!requestDto.getUsername().equals(updatedUser.getUsername())){
            if(userRepository.existsByUsername(requestDto.getUsername())){
                throw new NotChangeDataException("Пользователь зарегистрирован с таким именем");
            }
        }
        if(!requestDto.getEmail().equals(updatedUser.getEmail())){
            if(userRepository.existsByEmail(requestDto.getEmail())){
                throw new NotChangeDataException("Пользователь зарегистрирован с таким электронным адресом");
            }
        }

        updatedUser = userMapper.updateEntityFromDto(requestDto, updatedUser);
        updatedUser = userRepository.save(updatedUser);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void deleteById(Long id) {
        User deletedUser = findById(id);
        userRepository.delete(deletedUser);
    }

    @Override
    public Long getCount() {
        return 0L;
    }

    private boolean isRegisteredUser(String username, String email){
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    @Override
    public UserResponseDto create(UserRequestDto request) {
        return null;
    }
}
