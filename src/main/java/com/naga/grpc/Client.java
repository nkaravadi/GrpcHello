package com.naga.grpc;

import com.naga.grpc.proto.GreeterGrpc;
import com.naga.grpc.proto.Hello;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        String name = System.getenv("name");
        if (name == null || name.isEmpty()) {
            name = "World";
        }

        String host = System.getenv("host");
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }

        String portStr = System.getenv("port");
        if (portStr == null || portStr.isEmpty()) {
            portStr = "8999";
        }

        int port = Integer.parseInt(portStr);


        while(true) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            GreeterGrpc.GreeterBlockingStub greeterStub = GreeterGrpc.newBlockingStub(channel);
            Hello.HelloReply reply = greeterStub.sayHello(Hello.HelloRequest.newBuilder().setName(name).build());
            System.out.println("Got Response:");
            System.out.println(reply.getMessage());
            Thread.sleep(10000);
        }
    }
}
