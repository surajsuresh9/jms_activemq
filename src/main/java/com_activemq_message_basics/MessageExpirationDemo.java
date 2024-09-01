package com_activemq_message_basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;

public class MessageExpirationDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = (JMSContext) cf.createContext()) {
            JMSProducer producer = jmsContext.createProducer().send(queue, "Hello World!");
            producer.setTimeToLive(1000);
            producer.send(queue, "This message has ttl: 2s");
            Thread.sleep(2000);
            Message receivedMessage = jmsContext.createConsumer(queue).receive(2500);
            System.out.println(receivedMessage);
        }
    }
}
