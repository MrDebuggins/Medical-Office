package medical.gateway.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;


@Component
public class RoleFilterFunctions
{
    public static HandlerFilterFunction<ServerResponse, ServerResponse> validateAccess()
    {
        return (request, next) ->
        {
            return next.handle(request);
        };
    }

    public static HandlerFilterFunction<ServerResponse, ServerResponse> validateAccess2(String path, String token)
    {
        return (request, next) ->
        {
            ServerRequest modified = ServerRequest.from(request).header("X-Request-Id", "22").build();
            //ServerResponse response = next.handle(modified);
            System.out.println("here is the handler");

            return ServerResponse.status(400).build();
        };
    }
}
