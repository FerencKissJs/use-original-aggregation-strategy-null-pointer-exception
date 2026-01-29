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
        CamelExecutionException exceptionGood = assertThrows(
            CamelExecutionException.class,
            () -> producerTemplate.requestBody("direct:customAggregatorOk", "Bar Foo")
        );

        CamelExecutionException exceptionWrong = assertThrows(
            CamelExecutionException.class,
            () -> producerTemplate.requestBody("direct:reproducer", "Bar Foo")
        );

        assertNotNull(exceptionGood.getCause());
        assertInstanceOf(RuntimeException.class, exceptionGood.getCause());
        assertEquals("reproducer1", exceptionGood.getCause().getMessage());
        log.info("The wrong exception :", exceptionWrong.getCause());
        assertNotNull(exceptionWrong.getCause());
        assertInstanceOf(RuntimeException.class, exceptionWrong.getCause());
        assertEquals("reproducer1", exceptionWrong.getCause().getMessage());
    }
}