package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ReceptionistApplication.class)
class ReceptionistApplicationTests {

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void restTemplateTest() {
        assertNotNull(restTemplate, "RestTemplate dovrebbe essere inizializzato");
    }

}
