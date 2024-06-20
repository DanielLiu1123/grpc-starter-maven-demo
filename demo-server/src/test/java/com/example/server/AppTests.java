package com.example.server;

import com.example.v1.api.FooServiceGrpc;
import com.example.v1.api.GetFooRequest;
import com.example.v1.api.GetFooResponse;
import grpcstarter.client.EnableGrpcClients;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.protobuf.SimpleRequest;
import io.grpc.testing.protobuf.SimpleResponse;
import io.grpc.testing.protobuf.SimpleServiceGrpc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = AppTests.Cfg.class, properties = {
        "grpc.server.in-process.name=AppTests",
        "grpc.client.in-process.name=${grpc.server.in-process.name}"
}, webEnvironment = RANDOM_PORT)
class AppTests {

    @Autowired
    SimpleServiceGrpc.SimpleServiceBlockingStub stub;

    @Autowired
    TestRestTemplate rest;

    @LocalServerPort
    int port;

    @Test
    void testApp() {

        var msg = stub.unaryRpc(SimpleRequest.newBuilder()
                .setRequestMessage("World")
                .build()).getResponseMessage();

        assertThat(msg).isEqualTo("Hello, World");
    }

    @Test
    void testTranscoding() {

        // auto mapping
        // use request body first
        var resp = rest.postForEntity("http://localhost:" + port + "/grpc.testing.SimpleService/UnaryRpc?requestMessage=wow", """
                {"requestMessage": "World"}""", String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo("""
                {"responseMessage":"Hello, World"}""");

        // custom mapping
        resp = rest.getForEntity("http://localhost:" + port + "/foo/World", String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo("""
                {"message":"Hello, World"}""");
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @EnableGrpcClients(clients = SimpleServiceGrpc.SimpleServiceBlockingStub.class)
    static class Cfg {

        @Bean
        SimpleServiceGrpc.SimpleServiceImplBase simpleServiceImpl() {
            return new SimpleServiceGrpc.SimpleServiceImplBase() {
                @Override
                public void unaryRpc(SimpleRequest request, StreamObserver<SimpleResponse> responseObserver) {
                    var response = SimpleResponse.newBuilder()
                            .setResponseMessage("Hello, " + request.getRequestMessage())
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }

        @Bean
        FooServiceGrpc.FooServiceImplBase fooServiceImpl() {
            return new FooServiceGrpc.FooServiceImplBase() {
                @Override
                public void getFoo(GetFooRequest request, StreamObserver<GetFooResponse> responseObserver) {
                    var response = GetFooResponse.newBuilder()
                            .setMessage("Hello, " + request.getMessage())
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
