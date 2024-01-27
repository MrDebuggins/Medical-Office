package medical.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static medical.gateway.config.RoleFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.forward;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.REQUEST_ATTRIBUTE;
import static org.springframework.web.servlet.function.RouterFunctions.route;


@Configuration
public class RouteLocatorConfig
{
    @Bean
    public RouterFunction<ServerResponse> accessValidation()
    {
        return route()
                .add(route(RequestPredicates.path("/api/medical_office/patients/*/*/*/*/consultation/**"), http(URI.create("http://consult:8080"))).filter(validateAccess()))
                .add(route(RequestPredicates.path("/api/medical_office/patients/**"), http(URI.create("http://pdp:8080"))).filter(validateAccess()))
                .add(route(RequestPredicates.path("/api/medical_office/physicians/**"), http(URI.create("http://pdp:8080"))).filter(validateAccess()))
                //.add(route().GET("/api/medical_office/patients/*", forward(REQUEST_ATTRIBUTE)).after(responseProcessor()).build())
                .build();
    }
}
