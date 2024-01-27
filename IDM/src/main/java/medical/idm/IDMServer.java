package medical.idm;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.protobuf.Empty;
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
import java.util.logging.Logger;


public class IDMServer {
    private int port;
    private Server server;

    private Logger logger = Logger.getLogger("");

    private void start() throws IOException {
        int port = 8080;

        IdentityManagementService service;
        try {
            service = new IdentityManagementService();
            logger.info("Connected to Database");
        }catch (SQLException e)
        {
            logger.info("Database connection failed");
            return;
        }

        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(service)
                .build()
                .start();
        logger.info("Server started");

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
            logger.info("Server shutdown");
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

    private static class IdentityManagementService extends IdentityManagementServiceGrpc.IdentityManagementServiceImplBase
    {
        Logger logger = Logger.getLogger("");

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

        Connection connection = DriverManager.getConnection(dbUrl, user, password);

        //private static JedisPool pool = new JedisPool("localhost", 6379);

        private IdentityManagementService() throws SQLException {
        }

        @Override
        public void authorize(Main.Account req, StreamObserver<Main.Token> responseObserver)
        {
            logger.info("Authorization for" + req.getLogin());

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
                    return;
                }
            }catch (SQLException e)
            {
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
                return;
            }

            responseObserver.onNext(token);
            responseObserver.onCompleted();
        }

        @Override
        public void register(Main.Account req, StreamObserver<Main.IdentityResponse> responseObserver)
        {
            logger.info("Registration for " + req.getLogin());

            Main.IdentityResponse response;
            try {
                User user = dbGetUserByLogin(req.getLogin());

                if(user != null)
                {
                    responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
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
                    return;
                }

                user = dbGetUserByLogin(req.getLogin());
                if(user == null)
                {
                    responseObserver.onError(Status.CANCELLED.asRuntimeException());
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
                return;
            }

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void validate(Main.Token req, StreamObserver<Main.IdentityResponse> responseObserver)
        {
            logger.info("Token validation");

            DecodedJWT decodedJWT;
            try {
                JWTVerifier verifier = JWT.require(algorithm).build();

                decodedJWT = verifier.verify(req.getToken());

                long id = Long.parseLong(decodedJWT.getSubject());

                String query = "select * from Users where id = ?;";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, id);
                statement.setQueryTimeout(3);
                ResultSet resultSet = statement.executeQuery();

                JedisPool pool = new JedisPool("redis-idm", 6379);
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

                    pool.close();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                else
                {
                    pool.close();
                    responseObserver.onError(Status.UNAUTHENTICATED.asRuntimeException());
                }
            }
            catch (TokenExpiredException e)
            {
                System.out.println("Token expired!");
                responseObserver.onError(Status.RESOURCE_EXHAUSTED.asRuntimeException());
            }
            catch (Exception e)
            {
                responseObserver.onError(Status.UNAUTHENTICATED.asRuntimeException());
            }
        }

        @Override
        public void invalidate(Main.Token req, StreamObserver<Empty> responseObserver)
        {
            logger.info("Token invalidation");

            try {
                DecodedJWT decodedJWT;
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("medical_office")
                        .build();

                decodedJWT = verifier.verify(req.getToken());
                Long id = Long.decode(decodedJWT.getSubject());

                JedisPool pool = new JedisPool("redis-idm", 6379);
                Jedis jedis = pool.getResource();
                jedis.sadd(id.toString(), req.getToken());
                pool.close();

                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
            }catch (Exception e)
            {
                responseObserver.onError(Status.INTERNAL.asRuntimeException());
            }

        }

        @Override
        public void deleteUser(Main.Account req, StreamObserver<Empty> responseObserver)
        {
            logger.info("Failed to create PDP user. Deleting user");

            try
            {
                String deleteQ = "delete from Users where id=?;";
                PreparedStatement statement = connection.prepareStatement(deleteQ);
                statement.setLong(1, req.getRole());
                statement.execute();
            }
            catch (Exception e)
            {
                responseObserver.onError(Status.INTERNAL.asRuntimeException());
            }

            responseObserver.onCompleted();
        }

        private User dbGetUserByLogin(String login) throws SQLException
        {
            User idmUser = null;
            String query = "select * from Users where login = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
            {
                idmUser = new User();
                idmUser.setId(resultSet.getLong(1));
                idmUser.setLogin(resultSet.getString(2));
                idmUser.setPassword(resultSet.getString(3));
                idmUser.setRole(resultSet.getInt(4));
            }

            return idmUser;
        }

        private void dbDeleteExpiredTokens(long user_id)
        {
            JedisPool pool = new JedisPool("redis-idm", 6379);
            Jedis jedis = pool.getResource();
            Set<String> tokens = jedis.smembers(user_id + "");
            pool.close();

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
