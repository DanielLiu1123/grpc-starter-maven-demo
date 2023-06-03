package com.example.server;

import com.freemanan.starter.grpc.server.GrpcService;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.protobuf.SimpleRequest;
import io.grpc.testing.protobuf.SimpleResponse;
import io.grpc.testing.protobuf.SimpleServiceGrpc;

/**
 * @author Freeman
 */
@GrpcService
public class SimpleServiceImpl extends SimpleServiceGrpc.SimpleServiceImplBase {
    @Override
    public void unaryRpc(SimpleRequest request, StreamObserver<SimpleResponse> responseObserver) {
        SimpleResponse response = SimpleResponse.newBuilder()
                .setResponseMessage("Your request: " + request.getRequestMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
