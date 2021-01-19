package io.grpc.examples.helloworld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class HelloWorldServer {
  private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());

  private Server server;

  private void start() throws IOException {
    /* The port on which the server should run */
    int port = 50051;
    server = ServerBuilder.forPort(port)
        .addService(new GreeterImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        try {
          HelloWorldServer.this.stop();
        } catch (InterruptedException e) {
          e.printStackTrace(System.err);
        }
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final HelloWorldServer server = new HelloWorldServer();
    server.start();
    server.blockUntilShutdown();
  }

  private class GreeterImpl extends GreeterGrpc.GreeterImplBase {

//    @Override
//    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
//      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
//      responseObserver.onNext(reply);
//      responseObserver.onCompleted();
//    }
//
//    @Override
//    public void sayHelloAgain(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
//      HelloReply reply = HelloReply.newBuilder().setMessage("Hello again " + req.getName()).build();
//      responseObserver.onNext(reply);
//      responseObserver.onCompleted();
//    }

    @Override
    public void getProduct(EmptyMsg req, StreamObserver<ProductReply> responseObserver) {
      System.out.println("Get request from client");
      ProductReply reply = ProductReply.newBuilder().setMessage("Indomie Goreng Telur").build();
      System.out.println("Send response => " + reply);
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }


}
