package com.example.hotel_booking_service;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.model.repository.BookingRepository;
import com.example.hotel_booking_service.model.repository.HotelRepository;
import com.example.hotel_booking_service.model.repository.RoomRepository;
import com.example.hotel_booking_service.model.repository.UserRepository;
import com.example.hotel_booking_service.model.service.BookingService;
import com.example.hotel_booking_service.model.service.HotelService;
import com.example.hotel_booking_service.model.service.RoomService;
import com.example.hotel_booking_service.model.service.UserService;
import com.example.hotel_booking_service.web.mapper.HotelMapper;
import com.example.hotel_booking_service.web.mapper.RoomMapper;
import com.example.hotel_booking_service.web.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    private static final String USER_PARAM = "test-user";
    private static final String ADMIN_PARAM = "test-admin";
    private static final int hotelCount = 3;

    protected Long userId;
    protected Long adminId;
    protected List<Long> hotelIds;
    protected List<Long> roomIds;

    @Autowired
    protected TestDatabaseUtils testDatabaseUtils;


    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres).withReuse(true);

        postgreSQLContainer.start();

    }

    @DynamicPropertySource
    public static void registryProperties(DynamicPropertyRegistry registry){
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer :: getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer :: getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);

        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "hotel_booking_schema");
        registry.add("spring.jpa.properties.hibernate.hbm2ddl.create_namespaces", () -> "true");

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected HotelService hotelService;

    @Autowired
    protected RoomService roomService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected BookingService bookingService;

    @Autowired
    protected HotelRepository hotelRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected HotelMapper hotelMapper;

    @Autowired
    protected RoomMapper roomMapper;

    @Autowired
    protected UserMapper userMapper;


    @Autowired
    protected ObjectMapper objectMapper;




    protected void createUsers(){
        User user = new User();
        user.setUsername(USER_PARAM);
        user.setPassword(USER_PARAM);
        user.setEmail(USER_PARAM + "@mail.ru");
        user.setRoleType(RoleType.USER);
        user = userRepository.save(user);
        userId = user.getId();

        User admin = new User();
        admin.setUsername(ADMIN_PARAM);
        admin.setPassword(ADMIN_PARAM);
        admin.setEmail(ADMIN_PARAM + "@mail.ru");
        admin.setRoleType(RoleType.ADMIN);
        admin = userRepository.save(admin);
        adminId = admin.getId();
    }

    protected void createHotels(){
        List<Hotel> hotelList = new ArrayList<>();
        for(int i = 0; i <= hotelCount; i++){
            String hotelParam = "Hotel ";
            String addressParam = "Address ";
            String cityParam = "City ";
            Hotel hotel = new Hotel();
            hotel.setName(hotelParam + i);
            hotel.setTitle(cityParam + i + "_" +  hotelParam + i);
            hotel.setCity(cityParam + i);
            hotel.setAddress(addressParam + hotelParam + i);
            hotel.setDistanceFromCenter((double) i + 1);
            hotel.setRating((double) i + 1);
            hotel.setRatingCount(i);
            hotelList.add(hotel);
        }
        hotelRepository.saveAll(hotelList);
        hotelIds = hotelRepository.findAll().stream()
                .map(Hotel::getId)
                .toList();
    }

    protected void createRooms(){
        int roomCount = hotelCount * 3;
        String roomParam = "Room ";
        List<Room> roomList = new ArrayList<>();
        for(int i = 0; i <= hotelCount; i++){
            for(int j = 0; j <= roomCount; j++){
                Room room = new Room();
                Hotel hotel = hotelRepository.findById(hotelIds.get(i)).orElseThrow(() ->
                        new NoFoundEntityException("Hotel is not found by id"));
                room.setName(roomParam + j);
                room.setDescription(roomParam + hotel.getName() + "_" + j);
                room.setHotel(hotel);
                room.setNumber(j + 1);
                room.setMaxPeople(j + 1);
                room.setPrice(BigDecimal.valueOf((j + 1) * 1000L));
                roomList.add(room);
            }
        }
        roomRepository.saveAll(roomList);
        roomIds = roomRepository.findAll().stream()
                .map(Room::getId)
                .toList();
    }

    @BeforeEach
    @Transactional
    public void start(){
        createHotels();
        createRooms();
        createUsers();
    }

    @AfterEach
    @Transactional
    public void clearContainer(){
        testDatabaseUtils.resetDatabase();
    }


}
