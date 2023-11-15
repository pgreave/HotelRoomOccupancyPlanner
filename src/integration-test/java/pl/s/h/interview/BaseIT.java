package pl.s.h.interview;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = Main.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext
@ActiveProfiles({"integration-test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIT {

    @LocalServerPort
    private int port = 0;

    @BeforeEach
    public void setupRestAssured() {
        RestAssured.port = port;
    }
}
