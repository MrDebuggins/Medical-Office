package medical.main;

import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.InsecureServerCredentials;
import io.grpc.stub.StreamObserver;
import medical.main.proto.IdentityManagementServiceGrpc;
import medical.main.proto.Main;

import java.io.IOException;

public class IDMServer {
    private int port;
    private Server server;

    private void start() throws IOException {
        int port = 50051;
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new IdentityManagementService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                IDMServer.this.stop();
            }
        });
    }

    private void stop(){
        if(server != null){
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if(server != null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        final IDMServer IDM_server = new IDMServer();
        IDM_server.start();
        IDM_server.blockUntilShutdown();
    }

    private static class IdentityManagementService extends IdentityManagementServiceGrpc.IdentityManagementServiceImplBase {
        @Override
        public void authorize(Main.Account req, StreamObserver<Main.IdentityResponse> responseObserver)
        {
            responseObserver.onNext(verifyCredentials(req));
            responseObserver.onCompleted();
        }

        private Main.IdentityResponse verifyCredentials(Main.Account acc)
        {
            if(acc.getLogin().equals("bubn"))
            {
                return Main.IdentityResponse.newBuilder()
                        .setId(1)
                        .setRole(1)
                        .setToken("lool").build();
            }
            else
            {
                return Main.IdentityResponse.newBuilder()
                        .setId(2)
                        .setRole(2)
                        .setToken("not bubn").build();
            }
        }
    }
}
