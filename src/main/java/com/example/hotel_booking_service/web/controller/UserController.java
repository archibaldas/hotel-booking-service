package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.service.UserService;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import com.example.hotel_booking_service.web.dto.response.ErrorResponse;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API пользователей")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получение пользователя по Id",
            tags = {"get_by_id"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Пользователь найден по Id",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404",
                            description = "Пользователь не найден",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @LogExecution
    public UserResponseDto getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @Operation(summary = "Получение пользователя по имени пользователя",
            tags = {"get_by_username"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Пользователь найдет",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404",
                            description = "Комната не найдена",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public UserResponseDto getUserByUsername(@RequestParam String username){
        return userService.getUserByUsername(username);
    }

    @Operation(summary = "Регистрация нового пользователя",
            tags = {"create", "registration, new_user"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "201",
                            description = "Сохранение нового пользователя",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Некорректные данные",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecution
    public UserResponseDto createNewUser(@RequestBody UserRequestDto userRequestDto,
                                         @RequestParam RoleType roleType){
        return userService.createNewUser(userRequestDto, roleType);
    }

    @Operation(summary = "Обновление в базе данных записи пользователя",
            tags = {"update"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Данные пользователя обновлены",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Пользователь не имеет прав доступа",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "400",
                            description = "Некорректные данные",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404",
                            description = "Пользователь не найдена",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody UserRequestDto userRequestDto){
        return userService.update(id, userRequestDto);
    }

    @Operation(summary = "Удаление сохраненного пользователя",
            tags = {"delete"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "204",
                            description = "Комната обновлена"
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Пользователь не имеет прав доступа",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404",
                            description = "Пользователь не найден",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @LogExecution
    public void deleteUser(@PathVariable Long id){
        userService.deleteById(id);
    }
}
