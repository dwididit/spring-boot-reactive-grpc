package dev.dwidi.springbootreactivegrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class SpringBootReactiveGrpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactiveGrpcApplication.class, args);
    }

    @Bean
    public CommandLineRunner startGrpcServer() {
        return args -> {
            Server server = ServerBuilder.forPort(9090)
                    .addService(new UserServiceImpl())
                    .addService(ProtoReflectionService.newInstance())
                    .build();

            Logger logger = Logger.getLogger(SpringBootReactiveGrpcApplication.class.getName());
            logger.info("Starting gRPC server...");
            server.start();
            logger.info("gRPC server started, listening on " + server.getPort());

            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

            server.awaitTermination();
        };
    }
}
