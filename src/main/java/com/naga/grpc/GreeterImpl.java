package com.naga.grpc;
import com.naga.grpc.proto.GreeterGrpc;
import com.naga.grpc.proto.Hello;
import io.grpc.stub.StreamObserver;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    public static final String HELLO_TEMPLATE = "Hello %s!\nMessageId: %s, Source: %s";
    private final String sourceId = UUID.randomUUID().toString();
    private final AtomicLong counter = new AtomicLong();

    public GreeterImpl() {

    }
    @Override
    public void sayHello(Hello.HelloRequest request, StreamObserver<Hello.HelloReply> responseObserver) {
        String name = request.getName();
        System.out.println("Serving request with name = " + name);
        Hello.HelloReply.Builder replyBuilder = Hello.HelloReply.newBuilder();
        String reply = HELLO_TEMPLATE.formatted(name, counter.incrementAndGet(), sourceId);
        System.out.println("Response :" + reply);
        replyBuilder.setSource(sourceId).setMessage(reply);
        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }
}
