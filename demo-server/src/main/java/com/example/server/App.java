package com.example.server;

import com.freemanan.starter.grpc.client.EnableGrpcClients;
import com.freemanan.starter.httpexchange.EnableExchangeClients;
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
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.annotation.GetExchange;

@SpringBootApplication
@EnableGrpcClients("io.grpc")
@EnableExchangeClients
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

    @Bean
    ApplicationRunner runner(TestApi api, HttpExchangeApi httpExchangeApi) {
        return args -> {
            Assert.isTrue(api.test().equals("Hello World!"), "test() should return 'Hello World!'");
            Assert.isTrue(httpExchangeApi.testHttpExchangeApi().equals("testHttpExchangeApi"), "testHttpExchangeApi() should return 'testHttpExchangeApi'");
            try {
                api.testDefault();
            } catch (HttpStatusCodeException e) {
                Assert.isTrue(e.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "testDefault() should return 501");
            }
            try {
                httpExchangeApi.testHttpExchangeApiDefault();
            } catch (HttpStatusCodeException e) {
                Assert.isTrue(e.getStatusCode().equals(HttpStatus.NOT_IMPLEMENTED), "testHttpExchangeApiDefault() should return 501");
            }
        };
    }

    interface TestApi {
        @GetMapping("/test")
        String test();

        @GetMapping("/test-default")
        default String testDefault() {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    interface HttpExchangeApi {
        @GetExchange("/testHttpExchangeApi")
        String testHttpExchangeApi();

        @GetExchange("/testHttpExchangeApiDefault")
        default String testHttpExchangeApiDefault() {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @RestController
    static class Ctl implements TestApi, HttpExchangeApi {
        @Override
        public String test() {
            return "Hello World!";
        }

        @Override
        public String testHttpExchangeApi() {
            return "testHttpExchangeApi";
        }
    }

}
