package medical.idm;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.InsecureServerCredentials;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import medical.idm.entities.User;
import medical.idm.proto.IdentityManagementServiceGrpc;
import medical.idm.proto.Main;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.sql.*;
import java.util.Set;
import java.util.UUID;


public class IDMServer {
    private int port;
    private Server server;

    private void start() throws IOException {
        int port = 50200;

        IdentityManagementService service;
        try {
            service = new IdentityManagementService();
        }catch (SQLException e)
        {
            //TODO log
            System.out.println("Failed to connect to database");
            return;
        }

        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(service)
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

    public static void main(String[] args){
        try {
            final IDMServer IDM_server = new IDMServer();
            IDM_server.start();
            IDM_server.blockUntilShutdown();
        }catch (Exception e) {System.exit(1);}
    }

    private static class IdentityManagementService extends IdentityManagementServiceGrpc.IdentityManagementServiceImplBase {
        private static String dbUrl = "jdbc:mariadb://mariadb-idm:3306/User";
        private static String user = "root";
        private static String password = "idmdev";
        private static Algorithm algorithm = Algorithm.HMAC256("" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠞⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠲⡑⢄⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡼⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢮⣣⡀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠱⡀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢣⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⡞⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡆⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⡼⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⡤⠤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣹⠀\n" +
                "⠀⠀⠀⠀⢀⣀⣀⣸⢧⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠞⣩⡤⠶⠶⠶⠦⠤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⡇⡇\n" +
                "⠀⠀⠀⣰⣫⡏⠳⣏⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠚⠁⠀⠀⠀⠀⠀⠀⠙⢿⣿⣶⣄⡀⠀⠀⢀⡀⠀⠀⠀⠀⠀⡀⡅⡇\n" +
                "⠀⠀⢰⡇⣾⡇⠀⠙⣟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣠⣴⣶⠿⠛⠻⢿⣶⣤⣍⡙⢿⣿⣷⣤⣾⡇⣼⣆⣴⣷⣿⣿⡇⡇\n" +
                "⠀⠀⢸⡀⡿⠁⠀⡇⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⣿⣯⠴⢲⣶⣶⣶⣾⣿⣿⣿⣷⠹⣿⣿⠟⢰⣿⣿⣿⠿⣿⣿⣿⠁\n" +
                "⠀⠀⠈⡇⢷⣾⣿⡿⢱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠹⣌⠳⣼⣿⣿⣿⣻⣿⣿⣿⣿⡇⠈⠁⢰⣿⣿⣿⣿⣶⣾⣿⣿⠀\n" +
                "⠀⠀⠀⣷⠘⠿⣿⡥⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠃⠌⠉⣿⣿⣿⣿⣿⣿⠟⠃⠀⢀⡿⣿⣿⣿⣿⣿⣿⣿⡞⠀\n" +
                "⠀⠀⠀⢸⡇⠀⠹⠗⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⡿⠟⠉⠉⠀⠀⠀⠈⢃⣿⣿⣿⣿⣿⣿⡻⠀⠀\n" +
                "⠀⠀⠀⠈⢧⠀⠀⠏⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⠁⠀⠀\n" +
                "⠀⠀⠀⠀⠈⢳⠶⠞⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⠆⠀⠀⠊⠁⠀⠀⠀⠀⠸⣿⣿⣿⣿⣿⣿⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⣼⣿⣀⡰⠀⠀⣤⣄⠀⠀⠀⠀⢹⣿⣿⣿⣿⢻⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠹⣿⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠙⣿⣿⣿⡏⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⣄⢠⣤⣶⣤⣀⠀⢀⣶⣶⣶⣿⣿⠟⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⠖⠁⠀⠀⠀⠀⠀⠻⣿⣿⣥⣤⣯⣥⣾⣿⣿⣿⣿⠋⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⣰⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡼⠁⠀⠀⠀⠠⠀⠀⠀⠀⠈⣿⣿⣼⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⡰⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠊⠀⠀⠀⣠⣰⣄⡀⠀⢀⣀⣀⣛⣟⣿⣿⣿⣿⣿⣿⡿⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⣠⠜⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣼⠾⠛⠛⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀\n" +
                "⠾⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⡟⠀⠀⠀⠀⠠⣄⣉⣉⣻⣿⣿⣿⣿⣿⣿⡟⠧⢄⡀⠀⠀⠀⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⠅⠀⠀⠀⠀⠘⠉⠹⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀⠀⠉⠓⠢⣄⠀⠀⠀\n" +
                "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣉⣿⣿⣿⣿⣿⣿⣿⣿⣷⣻⡄⠀⠀⢀⡑⠢⠄");
        private static JedisPool pool = new JedisPool("redis_idm", 6379);

        Connection connection = DriverManager.getConnection(dbUrl, user, password);

        private IdentityManagementService() throws SQLException {
        }

        @Override
        public void authorize(Main.Account req, StreamObserver<Main.Token> responseObserver)
        {
            Main.Token token;

            try {
                User user = dbGetUserByLogin(req.getLogin());

                if(user != null)
                {
                    String jwt = JWT.create()
                            .withIssuer("medical_office")
                            .withSubject(user.getId().toString())
                            .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                            .withJWTId(UUID.randomUUID().toString())
                            .withClaim("role", user.getRole())
                            .sign(algorithm);

                    token = Main.Token.newBuilder().setToken(jwt).build();
                    dbDeleteExpiredTokens(user.getId());
                }
                else
                {
                    responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
                    responseObserver.onCompleted();
                    return;
                }
            }catch (SQLException e)
            {
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
                responseObserver.onCompleted();
                return;
            }

            responseObserver.onNext(token);
            responseObserver.onCompleted();
        }

        @Override
        public void register(Main.Account req, StreamObserver<Main.IdentityResponse> responseObserver)
        {
            Main.IdentityResponse response;

            try {
                User user = dbGetUserByLogin(req.getLogin());

                if(user != null)
                {
                    responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
                    responseObserver.onCompleted();
                    return;
                }
                String insertQ = "insert into Users (login,password,role) values(?,?,?);";
                PreparedStatement insert = connection.prepareStatement(insertQ);
                insert.setString(1, req.getLogin());
                insert.setString(2, req.getPassword());
                insert.setInt(3, req.getRole());

                if(insert.executeUpdate() == 0)
                {
                    responseObserver.onError(Status.CANCELLED.asRuntimeException());
                    responseObserver.onCompleted();
                    return;
                }

                user = dbGetUserByLogin(req.getLogin());
                if(user == null)
                {
                    responseObserver.onError(Status.CANCELLED.asRuntimeException());
                    responseObserver.onCompleted();
                    return;
                }

                response = Main.IdentityResponse
                        .newBuilder()
                        .setId(user.getId())
                        .setRole(user.getRole())
                        .build();
            }catch (SQLException e)
            {
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
                responseObserver.onCompleted();
                return;
            }

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void validate(Main.Token req, StreamObserver<Main.IdentityResponse> responseObserver)
        {
            DecodedJWT decodedJWT;
            try {
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("medical_office")
                        .build();

                decodedJWT = verifier.verify(req.getToken());

                long id = Long.parseLong(decodedJWT.getSubject());

                String query = "select * from Users where id = ?;";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                if(
                        resultSet.next() &&
                        resultSet.getInt(4) == decodedJWT.getClaim("role").asInt() &&
                        !pool.getResource().sismember(id + "", req.getToken()))
                {
                    Main.IdentityResponse response = Main.IdentityResponse
                            .newBuilder()
                            .setId(id)
                            .setRole(decodedJWT.getClaim("role").asInt())
                            .build();

                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                else
                {
                    throw Status.UNAUTHENTICATED.asRuntimeException();
                }
            } catch (Exception e)
            {
                throw Status.UNAUTHENTICATED.asRuntimeException();
            }
        }

        @Override
        public void invalidate(Main.Token req, StreamObserver<Main.InvalidationResponse> responseObserver)
        {
            try {
                DecodedJWT decodedJWT;
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("medical_office")
                        .build();

                decodedJWT = verifier.verify(req.getToken());
                Long id = Long.decode(decodedJWT.getSubject());

                Jedis jedis = pool.getResource();
                jedis.sadd(id.toString(), req.getToken());

                Main.InvalidationResponse resp = Main.InvalidationResponse.newBuilder().setSuccess(true).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }catch (Exception e)
            {
                throw Status.INTERNAL.asRuntimeException();
            }

        }

        private User dbGetUserByLogin(String login) throws SQLException
        {
            String query = "select * from Users where login = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            User user = null;
            if(resultSet.next())
            {
                user = new User();
                user.setId(resultSet.getLong(1));
                user.setLogin(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setRole(resultSet.getInt(4));
            }

            return user;
        }

        private void dbDeleteExpiredTokens(long user_id)
        {
            Jedis jedis = pool.getResource();
            Set<String> tokens = jedis.smembers(user_id + "");

            for(String token : tokens)
            {
                try {
                    DecodedJWT decodedJWT;
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer("medical_office")
                            .build();

                    decodedJWT = verifier.verify(token);
                }catch (TokenExpiredException e)
                {
                    jedis.srem(user_id + "", token);
                }
            }
        }
    }
}
