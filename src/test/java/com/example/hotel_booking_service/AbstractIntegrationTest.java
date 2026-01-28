package com.example.hotel_booking_service;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.model.entity.*;
import com.example.hotel_booking_service.repository.BookingRepository;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.service.BookingService;
import com.example.hotel_booking_service.statistics.event.StatisticRepository;
import com.example.hotel_booking_service.statistics.listner.StatisticEventListener;
import com.example.hotel_booking_service.statistics.publisher.EventPublisher;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationTest {

    protected static final String USER_PARAM = "test-user";
    protected static final String ADMIN_PARAM = "test-admin";
    private static final int hotelCount = 3;

    protected Long userId;
    protected Long adminId;
    protected List<Long> hotelIds;
    protected List<Long> roomIds;
    protected Long bookingId;

    @Autowired
    protected TestDatabaseUtils testDatabaseUtils;

    @Container
    protected static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0")
                    .asCompatibleSubstituteFor("apache/kafka")
    ).withReuse(true);

    @Container
    protected static final MongoDBContainer MONGO = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres).withReuse(true);

        postgreSQLContainer.start();
        KAFKA.start();
        MONGO.start();

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

        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);

        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("spring.kafka.consumer.group-id", () -> "test-group");
    }

    @Autowired
    protected MockMvc mockMvc;

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
    protected EventPublisher publisher;

    @Autowired
    protected StatisticEventListener listener;

    @Value("${app.kafka.booking-statistic-topic}")
    protected String statisticTopic;

    @Autowired
    protected StatisticRepository statisticRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected void createUsers(){
        UserRole role = UserRole.from(RoleType.USER);
        User user = new User();
        user.setUsername(USER_PARAM);
        user.setPassword(passwordEncoder.encode(USER_PARAM));
        user.setEmail(USER_PARAM + "@mail.ru");
        user.setRoles(Collections.singletonList(role));
        role.setUser(user);
        user = userRepository.save(user);
        userId = user.getId();

        role = UserRole.from(RoleType.ADMIN);

        User admin = new User();
        admin.setUsername(ADMIN_PARAM);
        admin.setPassword(passwordEncoder.encode(ADMIN_PARAM));
        admin.setEmail(ADMIN_PARAM + "@mail.ru");
        admin.setRoles(Collections.singletonList(role));
        role.setUser(admin);
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

    private void createBooking(){

        LocalDate nowDate = LocalDate.now();
        LocalDate arrivalDate = nowDate.plusDays(1);
        LocalDate departureDate = nowDate.plusDays(5);


        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setUserId(userId);
        requestDto.setRoomId(roomIds.get(3));
        requestDto.setArrivalDate(arrivalDate);
        requestDto.setDepartureDate(departureDate);

        com.example.hotel_booking_service.web.dto.response.BookingResponseDto responseDto = bookingService
                .create(requestDto);
        bookingId = responseDto.getId();
    }

    @BeforeEach
    @Transactional
    public void start(){
        createHotels();
        createRooms();
        createUsers();
        createBooking();
    }

    @AfterEach
    @Transactional
    public void clearContainer(){
        testDatabaseUtils.resetDatabase();
    }


}
