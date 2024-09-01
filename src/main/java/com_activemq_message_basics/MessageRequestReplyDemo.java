package com_activemq_message_basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessageRequestReplyDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
//        Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = (JMSContext) cf.createContext()) {

            JMSProducer reqProducer = jmsContext.createProducer();
            TemporaryQueue replyQueue = jmsContext.createTemporaryQueue();
            TextMessage textMessage1 = jmsContext.createTextMessage("Hello World! 1");
            // set replyTo
            textMessage1.setJMSReplyTo(replyQueue);
            reqProducer.send(requestQueue, textMessage1);
            System.out.println(textMessage1.getJMSMessageID());

            JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            TextMessage receivedTextMessage1 = (TextMessage) consumer.receive();
            System.out.println(receivedTextMessage1.getText());

            JMSProducer replyProducer = jmsContext.createProducer();
            TextMessage replyMessage = jmsContext.createTextMessage("This is a text message");
            replyMessage.setJMSCorrelationID(receivedTextMessage1.getJMSMessageID());

            replyProducer.send(receivedTextMessage1.getJMSReplyTo(), replyMessage);

            JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
//            String receivedTextMessage2 = replyConsumer.receiveBody(String.class);
            TextMessage receivedTextMessage = (TextMessage) replyConsumer.receive();
            System.out.println(receivedTextMessage.getJMSCorrelationID() + ": " + receivedTextMessage.getText());

//            Hello World! 1
//            Hello World! 2

        }
    }
}
