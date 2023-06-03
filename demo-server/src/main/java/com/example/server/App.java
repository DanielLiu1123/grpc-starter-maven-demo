package com.example.server;

import com.freemanan.starter.grpc.client.EnableGrpcClients;
import io.grpc.testing.protobuf.SimpleRequest;
import io.grpc.testing.protobuf.SimpleResponse;
import io.grpc.testing.protobuf.SimpleServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableGrpcClients("io.grpc")
public class App implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    SimpleServiceGrpc.SimpleServiceBlockingStub simpleStub;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SimpleResponse resp = simpleStub.unaryRpc(
                SimpleRequest.newBuilder()
                        .setRequestMessage("Hello")
                        .build()
        );
        log.info("Response: {}", resp.getResponseMessage());
    }
}
