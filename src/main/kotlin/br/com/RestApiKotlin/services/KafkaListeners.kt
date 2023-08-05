package br.com.RestApiKotlin.services

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArrayList

@Component
class KafkaListeners {

    private val messages: MutableList<String> = CopyOnWriteArrayList();

    @KafkaListener(
        topics = ["devtopic"],
        groupId = "groupId"
    )
    fun listener(message: String){
        System.out.println("Listner received: " + message);
        messages.add(message);
    }

    fun consumeAllMessages(): List<String> {
        return messages.toList();
    }
}