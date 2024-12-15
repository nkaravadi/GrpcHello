package com.naga.grpc;

import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ApplicationProtocolConfig;
import io.grpc.netty.shaded.io.netty.handler.ssl.ApplicationProtocolNames;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 0 && args[0].equals("client")) {
            Client.main(args);
        } else {
            //Get path of cert file from env or use default
            String certFilePath = System.getenv("ç");
            if (certFilePath == null || certFilePath.isEmpty()) {
                certFilePath = "/Users/nagakaravadi/dev/OpenShift/tls.crt";
            }
            File certFile = new File(certFilePath);

            //Get Keyfile path from env or use default
            String keyFilePath = System.getenv("keyFile");
            if (keyFilePath == null || keyFilePath.isEmpty()) {
                keyFilePath = "/Users/nagakaravadi/dev/OpenShift/tls.key";
            }
            File keyFile = new File(keyFilePath);

            String portStr = System.getenv("port");
            if (portStr == null || portStr.isEmpty()) {
                portStr = "8999";
            }

            int port = Integer.parseInt(portStr);

//            Server server = ServerBuilder
//                    .forPort(port)
//                    .addService(new GreeterImpl())
//                    .build();
            SslContext sslContext = SslContextBuilder
                    .forServer(certFile, keyFile)
                    .applicationProtocolConfig(new ApplicationProtocolConfig(
                            ApplicationProtocolConfig.Protocol.ALPN,
                            ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                            ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                            ApplicationProtocolNames.HTTP_2))
                    .build();
            Server server = NettyServerBuilder
                    .forPort(port)
                    .sslContext(sslContext)
                    .addService(new GreeterImpl())
                    .build();
            server.start();

            System.out.println("Server Started on port: " + port);
            server.awaitTermination();
        }
    }
}
