package route;

import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ReproducerRoute extends EndpointRouteBuilder {


    @Override
    public void configure() throws Exception {
        from(direct("reproducer"))
            .errorHandler(noErrorHandler())
            .recipientList(constant(
                "direct:reproducer1," +
                    "direct:reproducer2"))
            .aggregationStrategy(AggregationStrategies.useOriginal());

        from(direct("customAggregatorOk"))
            .recipientList(constant(
                "direct:reproducer1," +
                    "direct:reproducer2"))
            .aggregationStrategy(new CustomAggregatorStrategy());

        from(direct("reproducer1"))
            .log("reproducer1")
            .process(exchange -> {throw new RuntimeException("reproducer1");});
        from(direct("reproducer2"))
            .log("reproducer2");
    }
}
