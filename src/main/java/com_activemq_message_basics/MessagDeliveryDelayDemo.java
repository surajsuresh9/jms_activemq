package com_activemq_message_basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;

public class MessagDeliveryDelayDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = (JMSContext) cf.createContext()) {
            JMSProducer producer = jmsContext.createProducer().send(queue, "Hello World!");
            producer.setDeliveryDelay(3000);
            producer.send(queue, "This message has ttl: 2s");

            Message receivedMessage = jmsContext.createConsumer(queue).receive(2500);
            System.out.println(receivedMessage);
        }
    }
}
