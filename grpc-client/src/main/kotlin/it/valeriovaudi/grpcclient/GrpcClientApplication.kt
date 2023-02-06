package it.valeriovaudi.grpcclient

import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import it.valeriovaudi.model.GreetingsServiceGrpc.newBlockingStub
import it.valeriovaudi.model.HelloRequest
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class GrpcClientApplication {

    @Bean
    fun client(): GreetingsClient {
        val channel: ManagedChannel = ManagedChannelBuilder
            .forAddress("localhost",9090)
            .usePlaintext()
            .build()
        return GreetingsClient(channel)
    }
}

fun main(args: Array<String>) {
    runApplication<GrpcClientApplication>(*args)
}

class GreetingsClient(private val channel: Channel) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val greetingsServiceBlockingStub = newBlockingStub(channel)
        val sayHello = greetingsServiceBlockingStub
            .sayHello(
                HelloRequest.newBuilder()
                    .setName("Valerio")
                    .build()
            )
        println(sayHello)
    }

}