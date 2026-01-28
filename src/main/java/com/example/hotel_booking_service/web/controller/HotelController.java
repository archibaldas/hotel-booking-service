package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.model.filter.HotelFilter;
import com.example.hotel_booking_service.service.HotelService;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.ErrorResponse;
import com.example.hotel_booking_service.web.dto.response.HotelListResponseDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
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
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotel API", description = "API для управления отелями ")
public class HotelController {

    private final HotelService hotelService;


    @Operation(summary = "Получение постраничного списка, или списка с фильтрами",
            tags = {"get_all", "filter"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Все отели найдены, выведен постраничный список",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HotelListResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Список пуст",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))

                            }),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public HotelListResponseDto getHotelsByFilter(@Valid HotelFilter hotelFilter) {
        return hotelService.findAllByFilter(hotelFilter);
    }

    @Operation(summary = "Получение отеля по Id",
            tags = {"get_by_Id"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Отель по Id найден",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HotelResponseDto.class)
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Отель не найден",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public HotelResponseDto getHotelById(@PathVariable Long id) {
        return hotelService.getHotelResponseById(id);
    }

    @Operation(summary = "Создание в базе данных записи отеля",
            tags = {"create"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "201",
                            description = "Отель сохранен",
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
    public HotelResponseDto createHotel(@Valid @RequestBody HotelRequestDto requestDto) {
        return hotelService.create(requestDto);
    }


    @Operation(summary = "Обновление в базе данных записи отеля",
            tags = {"update"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Отель обновлен",
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
                            }),
                    @ApiResponse(responseCode = "404",
                            description = "Отель не найден",
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
    public HotelResponseDto updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequestDto requestDto) {
        return hotelService.update(id, requestDto);
    }

    @Operation(summary = "Удаление отеля",
            tags = {"delete"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "204",
                            description = "Отель удален"
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
                            description = "Отель не найден",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @LogExecution
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteById(id);
    }

    @Operation(summary = "Обновление рейтинга отеля",
            tags = {"update_rating"})
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Рейтинг отеля обновлен",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HotelResponseDto.class)
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
                            description = "Отель не найден",
                            content = {
                                    @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PutMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    @LogExecution
    public HotelResponseDto updateHotelRating(@PathVariable Long id,
                                              @RequestParam int newMark) {
        return hotelService.updateHotelRating(id, newMark);
    }
}
