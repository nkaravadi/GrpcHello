package com.naga.grpc;

import com.naga.grpc.proto.GreeterGrpc;
import com.naga.grpc.proto.Hello;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ApplicationProtocolConfig;
import io.grpc.netty.shaded.io.netty.handler.ssl.ApplicationProtocolNames;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.io.File;

public class Client {
    public static void main(String[] args) throws InterruptedException, SSLException {
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

        // Load the self-signed certificate
        File certFile = new File("/Users/nagakaravadi/dev/OpenShift/tls.crt");
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(certFile)
                .applicationProtocolConfig(new ApplicationProtocolConfig(
                        ApplicationProtocolConfig.Protocol.ALPN,
                        ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                        ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL,
                        ApplicationProtocolNames.HTTP_2))
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        while (true) {
            ManagedChannel channel = NettyChannelBuilder.forAddress(host, port)
                    .sslContext(sslContext)
                    .build();
            GreeterGrpc.GreeterBlockingStub greeterStub = GreeterGrpc.newBlockingStub(channel);
            Hello.HelloReply reply = greeterStub.sayHello(Hello.HelloRequest.newBuilder().setName(name).build());
            System.out.println("Got Response:");
            System.out.println(reply.getMessage());
            Thread.sleep(10000);
        }
    }
}
