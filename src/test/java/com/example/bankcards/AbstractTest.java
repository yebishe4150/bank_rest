package com.example.bankcards;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardEncryptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureMockMvc(addFilters = true)
@Transactional
public abstract class AbstractTest {

    protected static final String PASSWORD = "$2a$10$abcdefghijklmnopqrstuv";
    protected static final String USER_NAME = "user@test.com";
    protected static final String ADMIN_NAME = "admin@test.com";

    protected static final String NEW_USER_EMAIL = "newuser@test.com";
    protected static final String OTHER_USER_EMAIL = "otheruser@test.com";
    protected static final String TARGET_EMAIL = "target@test.com";

    protected static final String DEFAULT_NEW_PASSWORD = "password123";

    protected static final String NEW_ADMIN = "new-admin@test.com";
    protected static final String NEW_USER = "new-user@test.com";
    protected static final String ANY_PASSWORD = "pass123";


    protected String tokenOf(User user) {
        return "Bearer " + jwtTokenProvider.generateToken(user.getUsername());
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected CardService cardService;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected CardEncryptor cardEncryptor;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEachTest() {
    }

    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
