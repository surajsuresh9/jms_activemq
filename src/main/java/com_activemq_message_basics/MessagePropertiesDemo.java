package com_activemq_message_basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessagePropertiesDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = (JMSContext) cf.createContext()) {

            TextMessage textMessage = jmsContext.createTextMessage("Hello World!");
            textMessage.setBooleanProperty("loggedIn", true);
            textMessage.setStringProperty("userToken", "abc123");

            JMSProducer producer = jmsContext.createProducer().send(queue, textMessage);


            Message message = jmsContext.createConsumer(queue).receive(5000);
            System.out.println(message);
            System.out.println("loggedIn: " + message.getBooleanProperty("loggedIn"));
            System.out.println("userToken: " + message.getStringProperty("userToken"));

//            loggedIn: true
//            userToken: abc123

        }
    }
}
