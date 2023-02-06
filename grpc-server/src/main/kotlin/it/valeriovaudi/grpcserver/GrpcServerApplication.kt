package it.valeriovaudi.grpcserver

import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import it.valeriovaudi.model.GreetingsServiceGrpc
import it.valeriovaudi.model.HelloReply
import it.valeriovaudi.model.HelloRequest
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableConfigurationProperties(ServerProperties::class)
class GrpcServerApplication {
    @Bean
    fun grpcServerStarter(serverProperties: ServerProperties) =
        GrpcServerStarter(serverProperties)
}

fun main(args: Array<String>) {
    runApplication<GrpcServerApplication>(*args)
}

class GrpcServerStarter(private val serverProperties: ServerProperties) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        println("STARTED")
        ServerBuilder
            .forPort(serverProperties.port)
            .addService(GreetingsService())
            .build()
            .let {
                it.start()
                    .awaitTermination()
            }
    }
}

class GreetingsService : GreetingsServiceGrpc.GreetingsServiceImplBase() {
    override fun sayHello(req: HelloRequest, responseObserver: StreamObserver<HelloReply?>) {
        val reply = HelloReply.newBuilder().setMessage("Hello " + req.name).build()
        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
}