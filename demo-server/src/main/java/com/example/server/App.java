package com.example.server;

import com.freemanan.starter.grpc.client.EnableGrpcClients;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.protobuf.SimpleRequest;
import io.grpc.testing.protobuf.SimpleResponse;
import io.grpc.testing.protobuf.SimpleServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableGrpcClients("io.grpc")
public class App extends SimpleServiceGrpc.SimpleServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void unaryRpc(SimpleRequest request, StreamObserver<SimpleResponse> responseObserver) {
        SimpleResponse response = SimpleResponse.newBuilder()
                .setResponseMessage("Your request: " + request.getRequestMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Bean
    ApplicationRunner runner(SimpleServiceGrpc.SimpleServiceBlockingStub simpleStub) {
        return args -> {
            SimpleResponse resp = simpleStub.unaryRpc(
                    SimpleRequest.newBuilder().setRequestMessage("Hello").build());
            log.info("Response: {}", resp.getResponseMessage());
        };
    }
}
