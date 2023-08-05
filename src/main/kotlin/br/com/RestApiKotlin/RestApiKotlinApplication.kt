package br.com.RestApiKotlin

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@SpringBootApplication
class RestApiKotlinApplication

fun main(args: Array<String>) {
	runApplication<RestApiKotlinApplication>(*args)
}

@Component
class KafkaMessageProducer(private val kafkaTemplate: KafkaTemplate<String, String>) : CommandLineRunner {
	override fun run(vararg args: String?) {
		kafkaTemplate.send("devtopic", "Funcionou!")
	}
}




