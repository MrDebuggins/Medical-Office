package medical.gateway.controllers;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.InsecureServerCredentials;
import io.grpc.ManagedChannelBuilder;
import medical.gateway.proto.IdentityManagementServiceGrpc;
import medical.gateway.proto.Main;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class GatewayController {
    IdentityManagementServiceGrpc.IdentityManagementServiceBlockingStub IDM_Server =
            IdentityManagementServiceGrpc
                    .newBlockingStub(ManagedChannelBuilder.forAddress("idm", 50051).usePlaintext().build());

    @PostMapping("/test/register")
    public String register(@RequestBody Map<String, String> accInfo)
    {
        String login = accInfo.get("login");
        String password = accInfo.get("password");

        Main.Account acc = Main.Account.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();

        try {
            Main.IdentityResponse resp = IDM_Server.register(acc);
            return resp.toString();
        }catch (Exception e)
        {return "4.. some error";}
    }

    @PostMapping("/test/auth")
    public String auth(@RequestBody Map<String, String> accInfo)
    {
        String login = accInfo.get("login");
        String password = accInfo.get("password");

        Main.Account acc = Main.Account.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build();

        return IDM_Server.authorize(acc).getToken();
    }
}
