package medical.gateway.controllers.pdp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class AdminController
{
    String PDPBaseURL = "http://localhost:45749";

    @GetMapping("/bubn")
    String bubn(){return "bubn";}

    @GetMapping("/patients")
    ResponseEntity<?> getPatients(/*@RequestHeader("Authorization") String token, */HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            //String token = request.getHeader("Authorization").split(" ")[1];
            //Main.IdentityResponse validationResp = IDM_Server.validate(Main.Token.newBuilder().setToken(token).build());
            if(0 == 0)
            {
                return new RestTemplate().getForEntity(PDPBaseURL + request.getRequestURI(), Object.class);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage() + "\n" + ex.getCause());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
