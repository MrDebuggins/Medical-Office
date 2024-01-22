package medical.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static medical.gateway.config.RoleFilterFunctions.validateAccess;
import static medical.gateway.config.RoleFilterFunctions.validateAccess2;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;


@Configuration
public class RouteLocatorConfig
{
    @Bean
    public RouterFunction<ServerResponse> validateLOL()
    {
        return route()
                .add(route(RequestPredicates.path("/api/medical_office/patients/*/*/*/*/consultation/**"), http(URI.create("http://localhost:8083"))).filter(validateAccess()))
                .add(route(RequestPredicates.path("/api/medical_office/patients/**"), http(URI.create("http://localhost:8080"))).filter(validateAccess()))
                .add(route(RequestPredicates.path("/api/medical_office/doctors/**"), http(URI.create("http://localhost:8080"))).filter(validateAccess()))
                .build();
    }
}
