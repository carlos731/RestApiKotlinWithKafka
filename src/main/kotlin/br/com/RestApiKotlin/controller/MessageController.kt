package br.com.RestApiKotlin.controller

import br.com.RestApiKotlin.model.request.MessageRequest
import br.com.RestApiKotlin.model.response.MessageResponse
import br.com.RestApiKotlin.services.KafkaListeners
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.message.ConsumerProtocolAssignment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("api/messages")
class MessageController (private val kafkaListener: KafkaListeners) {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>;

    @Autowired
    private lateinit var consumerFactory: ConsumerFactory<String, String>;

    @PostMapping
    fun enviar(@RequestBody request: MessageRequest) : ResponseEntity<MessageResponse> {
        kafkaTemplate.send("devtopic", request.message);
        return ResponseEntity.ok(MessageResponse(message = request.message));
    }

    @GetMapping
    fun findByTopic() : List<String> {
        return kafkaListener.consumeAllMessages();
    }

    @GetMapping("/buscar/{topic}/{partition}/{offset}")
    fun findAll(
        @PathVariable topic: String,
        @PathVariable partition: Int,
        @PathVariable offset: Long)
    : List<String> {
        val topicPartition = TopicPartition(topic, partition);

        val consumer: Consumer<String, String> = consumerFactory.createConsumer();
        consumer.assign(Collections.singleton(topicPartition));
        consumer.seek(topicPartition, offset);

        val records = consumer.poll(Duration.ofMillis(100));
        consumer.close();

        val mensagens = mutableListOf<String>();
        for (record in records) {
            val mensagem = record.value();
            mensagens.add(mensagem)
        }

        return mensagens;
    }

}