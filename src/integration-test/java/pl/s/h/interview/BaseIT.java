package pl.s.h.interview;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT, classes = Main.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext
@ActiveProfiles({"integration-test"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIT {

}
