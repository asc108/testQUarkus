import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class DockerCheckTest {

    @Container
    static GenericContainer<?> container = new GenericContainer<>("alpine:3.14")
            .withCommand("sh", "-c", "sleep 5 && echo 'Docker works!'");

    @Test
    void testContainerIsRunning() {
        assertTrue(container.isRunning());
        System.out.println("✅ TestContainers radi! Docker-in-Docker funkcioniše.");
    }
}