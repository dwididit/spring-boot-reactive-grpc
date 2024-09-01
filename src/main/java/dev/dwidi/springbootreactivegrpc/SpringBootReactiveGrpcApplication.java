package dev.dwidi.springbootreactivegrpc;

import dev.dwidi.springbootreactivegrpc.config.LoggingInterceptor;
import dev.dwidi.springbootreactivegrpc.repository.UserRepository;
import dev.dwidi.springbootreactivegrpc.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootReactiveGrpcApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootReactiveGrpcApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactiveGrpcApplication.class, args);
    }

    @Bean
    public CommandLineRunner startGrpcServer(UserRepository userRepository) {
        return args -> {
            Server server = ServerBuilder.forPort(9090)
                    .addService(new UserServiceImpl(userRepository))
                    .addService(ProtoReflectionService.newInstance())
                    .intercept(new LoggingInterceptor())
                    .build();

            logger.info("Starting gRPC server...");
            server.start();
            logger.info("gRPC server started, listening on {}", server.getPort());

            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

            server.awaitTermination();
        };
    }
}
