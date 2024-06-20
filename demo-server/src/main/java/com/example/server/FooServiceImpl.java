package com.example.server;

import com.example.v1.api.FooServiceGrpc;
import com.example.v1.api.GetFooRequest;
import com.example.v1.api.GetFooResponse;
import grpcstarter.server.GrpcService;
import io.grpc.stub.StreamObserver;

/**
 * @author Freeman
 */
@GrpcService
public class FooServiceImpl extends FooServiceGrpc.FooServiceImplBase {
    @Override
    public void getFoo(GetFooRequest request, StreamObserver<GetFooResponse> responseObserver) {
        GetFooResponse response = GetFooResponse.newBuilder()
                .setMessage("Your request: " + request.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
