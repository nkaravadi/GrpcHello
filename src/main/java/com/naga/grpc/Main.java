package com.naga.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length > 0 && args[0].equals("client")) {
            Client client = new Client();
            client.main(args);
        }
        else {
            String portStr = System.getenv("port");
            if (portStr == null || portStr.isEmpty()) {
                portStr = "8999";
            }

            int port = Integer.parseInt(portStr);

            Server server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build();
            server.start();
            System.out.println("Server Started on port: " + port);
            server.awaitTermination();
        }
    }
}