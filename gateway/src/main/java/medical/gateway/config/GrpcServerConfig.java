package medical.gateway.config;

import io.grpc.ManagedChannelBuilder;
import medical.gateway.proto.IdentityManagementServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcServerConfig
{
    @Bean
    public IdentityManagementServiceGrpc.IdentityManagementServiceBlockingStub IDM_Server()
    {
        return IdentityManagementServiceGrpc
                .newBlockingStub(ManagedChannelBuilder.forAddress("localhost", 50200).usePlaintext().build());
    }
}
