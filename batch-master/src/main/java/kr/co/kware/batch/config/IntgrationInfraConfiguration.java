package kr.co.kware.batch.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.channel.PollableAmqpChannel;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jms.DynamicJmsTemplate;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by chlee on 15. 4. 15..
 */
//@Configuration
public class IntgrationInfraConfiguration {

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Autowired
    private JmsTemplate jmsTemplate;

    @Bean
    public PollableChannel requestChannel() {
        return new PollableAmqpChannel("request", amqpTemplate);
    }

    @Bean
    public PollableChannel responseChannel() {
        return new PollableAmqpChannel("response", amqpTemplate);
    }

    @Bean
    public PollableChannel replyChannel() {
        return new PollableAmqpChannel("reply", amqpTemplate);
    }

    @ServiceActivator(inputChannel = "requestChannel")
    public MessageHandler jmsOutboundAdapter() {
        JmsTemplate jmsTemplate = new DynamicJmsTemplate();

        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate);
        handler.setDestinationName("replyChannel");

        return handler;
    }

}
