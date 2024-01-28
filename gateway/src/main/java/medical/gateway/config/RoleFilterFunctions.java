package medical.gateway.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseDecoder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponseWrapper;
import medical.gateway.entities.Doctor;
import medical.gateway.entities.Patient;
import medical.gateway.proto.IdentityManagementServiceGrpc;
import medical.gateway.proto.Main;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


@Component
public class RoleFilterFunctions
{
    private static IdentityManagementServiceGrpc.IdentityManagementServiceBlockingStub IDM_Server =
            IdentityManagementServiceGrpc.newBlockingStub(ManagedChannelBuilder
                    .forAddress("idm", 8080).usePlaintext().build());

    // patient resource
    private static String allPatientsRegex = "/api/medical_office/patients/";
    private static String onePatientRegex = "/api/medical_office/patients/\\w+";

    // physicians resource
    private static String allPhysiciansRegex = "/api/medical_office/physicians/";
    private static String onePhysiciansRegex = "/api/medical_office/physicians/\\w+";

    // appointment resource
    private static String appFromPatientRegex = "/api/medical_office/patients/\\w+/physicians(/\\w+)?";
    private static String appFromPhysicianRegex = "/api/medical_office/physicians/\\w+/patients(/\\w+)?";

    // consultation resource
    private static String consultationsRegex = "/api/medical_office/patients/\\w+/physicians/\\w+/\\w+/consultation(/\\w+)*";

    public static HandlerFilterFunction<ServerResponse, ServerResponse> validateAccess()
    {
        return (request, next) ->
        {
            long id;
            int role;

            try
            {
                String token = request.headers().firstHeader("Authorization")
                        .split(" ")[1];

                Main.Token idmToken = Main.Token.newBuilder().setToken(token).build();

                Main.IdentityResponse idmResp = IDM_Server.validate(idmToken);
                id = idmResp.getId();
                role = idmResp.getRole();
            }
            catch (StatusRuntimeException e) {return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();}
            catch (Exception e) {return ServerResponse.status(HttpStatus.BAD_REQUEST).build();}

            ServerRequest req = ServerRequest.from(request)
                    .header("X-Forwarded-Host", "localhost:8080").build();

            String path = req.requestPath().toString();

            if(path.matches(allPatientsRegex))
            {
                if(role == 0)
                    return next.handle(req);
                else if(role == 1 && req.method() == HttpMethod.GET)
                    return next.handle(req);
                else
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
            }

            if(path.matches(onePatientRegex))
            {
                if(role == 0)
                {
                    return next.handle(req);
                }
                else if(role == 1 && req.method() == HttpMethod.GET)
                {
                    return next.handle(req);
                }
                else
                {
                    ServerResponse check = checkPatientId(id, req);
                    if(check.statusCode() == HttpStatus.OK)
                    {
                        ServerResponse temp = next.handle(req);

                        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                        var cachedResponse = new ContentCachingResponseWrapper(ra.getResponse());
                        temp.writeTo(req.servletRequest(), cachedResponse, new ServerResponse.Context()
                        {
                            @Override
                            public List<HttpMessageConverter<?>> messageConverters()
                            {
                                List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
                                MappingJackson2HttpMessageConverter cnvrt = new MappingJackson2HttpMessageConverter();
                                list.add(cnvrt);
                                return list;
                            }
                        });

                        String regex = ",\"parent\".[^}]*}";
                        String newBody = new String(cachedResponse.getContentAsByteArray(),
                                "UTF-8").replaceFirst(regex, "");

                        return ServerResponse.from(temp).body(newBody);
                    }
                    else
                        return check;
                }
            }

            if(path.matches(onePhysiciansRegex))
            {
                if(role == 0)
                {
                    return next.handle(req);
                }
                else if(role == 2 && req.method() == HttpMethod.GET)
                {
                    return next.handle(req);
                }
                else
                {
                    ServerResponse check = checkDoctorId(id, req, 4);
                    if(check.statusCode() == HttpStatus.OK)
                    {
                        ServerResponse temp = next.handle(req);

                        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                        var cachedResponse = new ContentCachingResponseWrapper(ra.getResponse());
                        temp.writeTo(req.servletRequest(), cachedResponse, new ServerResponse.Context()
                        {
                            @Override
                            public List<HttpMessageConverter<?>> messageConverters()
                            {
                                List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
                                MappingJackson2HttpMessageConverter cnvrt = new MappingJackson2HttpMessageConverter();
                                list.add(cnvrt);
                                return list;
                            }
                        });

                        String regex = ",\"parent\".*true}";
                        String newBody = new String(cachedResponse.getContentAsByteArray(),
                                "UTF-8").replaceFirst(regex, "");

                        return ServerResponse.from(temp).body(newBody);
                    }
                    else
                        return check;
                }
            }

            if(path.matches(allPhysiciansRegex))
            {
                if(role == 0)
                    return next.handle(req);
                else if(role == 2 && req.method() == HttpMethod.GET)
                    return next.handle(req);
                else
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
            }

            if(path.matches(appFromPatientRegex))
            {
                if(role == 0 || role == 1)
                {
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                }
                else
                {
                    ServerResponse check = checkPatientId(id, req);
                    if(check.statusCode() == HttpStatus.OK)
                        return next.handle(req);
                    else
                        return check;
                }
            }

            if(path.matches(appFromPhysicianRegex))
            {
                if(role == 0 || role == 2)
                {
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                }
                else
                {
                    ServerResponse check = checkDoctorId(id, req, 4);
                    if(check.statusCode() == HttpStatus.OK)
                        return next.handle(req);
                    else
                        return check;
                }
            }

            if(path.matches(consultationsRegex))
            {
                if(id == 0)
                {
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                }
                else if(role == 1)
                {
                    ServerResponse check = checkDoctorId(id, req, 6);
                    if(check.statusCode() != HttpStatus.OK)
                        return check;

                    if(req.method() == HttpMethod.GET)
                        return next.handle(req);

                    String[] elemnts = request.requestPath().toString().split("/");
                    ResponseEntity<?> responseEntity = new RestTemplate()
                            .getForEntity("pdp:8080/api/medical_office/patients/"
                                    + elemnts[4] + "/physicians"
                                    + elemnts[6] + "?date="
                                    + elemnts[7], Object.class);

                    if(responseEntity.getStatusCode() == HttpStatus.OK)
                        return next.handle(req);
                    else
                        return ServerResponse.status(HttpStatus.BAD_REQUEST).build();
                }
                else
                {
                    ServerResponse check = checkPatientId(id, req);
                    if(check.statusCode() == HttpStatus.OK)
                        return next.handle(req);
                    else
                        return check;
                }
            }

            return ServerResponse.status(HttpStatus.NOT_FOUND).build();
        };
    }

    public static ServerResponse checkPatientId(long id, ServerRequest request)
    {
        try
        {
            String patientId = request.requestPath().toString().split("/")[4];
            ResponseEntity<Patient> responseEntity = new RestTemplate()
                    .getForEntity("http://pdp:8080/api/medical_office/patients/" + patientId, Patient.class);

            return ServerResponse.status(HttpStatus.OK).build();
        }
        catch (RestClientException e)
        {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }
    }

    private static ServerResponse checkDoctorId(long id, ServerRequest request, int pathIndex)
    {
        try
        {
            String doctorId = request.requestPath().toString().split("/")[pathIndex];
            ResponseEntity<Doctor> responseEntity = new RestTemplate()
                    .getForEntity("http://pdp:8080/api/medical_office/physicians/" + doctorId, Doctor.class);

            return ServerResponse.status(HttpStatus.OK).build();
        }
        catch (RestClientException e)
        {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
