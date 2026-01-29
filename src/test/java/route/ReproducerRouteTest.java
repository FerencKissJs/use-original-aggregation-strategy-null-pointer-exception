package route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@SpringBootTest
@Slf4j
class ReproducerRouteTest {

    @Autowired
    ProducerTemplate producerTemplate;

    @Test
    void recipientList_shouldThrowRuntimeExceptionFromReproducer1() {
        CamelExecutionException ex = assertThrows(
            CamelExecutionException.class,
      //      () -> producerTemplate.requestBody("direct:reproducer", "Bar Foo")
            () -> producerTemplate.requestBody("direct:customAggregatorOk", "Bar Foo")
        );

        log.info("", ex.getCause());
        // Camel wraps the underlying exception; verify the real cause + message
        assertNotNull(ex.getCause());
        assertInstanceOf(RuntimeException.class, ex.getCause());
        assertEquals("reproducer1", ex.getCause().getMessage());
    }
}