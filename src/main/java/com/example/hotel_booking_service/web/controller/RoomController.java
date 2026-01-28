package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.model.filter.RoomFilter;
import com.example.hotel_booking_service.service.RoomService;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.ErrorResponse;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import com.example.hotel_booking_service.web.dto.response.RoomListResponseDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
@Tag(name = "Room API", description = "API для управления комнатами")
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "Получение постраничного списка комнат с фильтрацией",
            tags = {"get_all", "filter"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Список комнат получен",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoomResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "400",
                            description = "Введены некорректные данные фильтрации",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404",
                            description = "Комнаты не найдены",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public RoomListResponseDto getRooms(@Valid RoomFilter filter){
        return roomService.findRoomsByFilter(filter);
    }

    @Operation(summary = "Получение комнаты по Id",
            tags = {"get_by_Id"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Комната получена по Id",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoomResponseDto.class)
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
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public RoomResponseDto getRoomById(@PathVariable Long id){
        return roomService.getRoomResponseById(id);
    }

    @Operation(summary = "Сохранение данных комнаты в базу",
            tags = {"create"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "201",
                            description = "Комната записана",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HotelResponseDto.class)
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
                            })
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @LogExecution
    public RoomResponseDto createRoom(@Valid @RequestBody RoomRequestDto roomRequestDto){
        return roomService.create(roomRequestDto);
    }

    @Operation(summary = "Обновление в базе данных записи комнаты",
            tags = {"update"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Комната обновлена",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoomResponseDto.class)
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
                            description = "Комната не найдена",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @LogExecution
    public RoomResponseDto updatedRoom(@PathVariable Long id,
                                       @Valid @RequestBody RoomRequestDto roomRequestDto){
        return roomService.update(id, roomRequestDto);
    }

    @Operation(summary = "Удаление комнаты",
            tags = {"delete"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "204",
                            description = "Комната удалена"
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
                            description = "Комната не найдена",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @LogExecution
    public void deleteRoom (@PathVariable Long id){
        roomService.deleteById(id);
    }
}
