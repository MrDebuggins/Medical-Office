package medical.gateway.config;

import io.grpc.ManagedChannelBuilder;
import medical.gateway.proto.IdentityManagementServiceGrpc;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestClientConfig
{
    @Bean
    public RestTemplate restClient()
    {
        return new RestTemplate();
    }

/*    @Bean
    public IdentityManagementServiceGrpc.IdentityManagementServiceBlockingStub grpcServer()
    {
        return IdentityManagementServiceGrpc.newBlockingStub(ManagedChannelBuilder
            .forAddress("idm", 8080).usePlaintext().build());
    }*/
}
