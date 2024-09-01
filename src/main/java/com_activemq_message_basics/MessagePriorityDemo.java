package com_activemq_message_basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessagePriorityDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");
        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = cf.createContext();) {
            // Create Producer
            JMSProducer producer = jmsContext.createProducer();
            String[] messages = new String[3];
            messages[0] = "Message One";
            messages[1] = "Message Two";
            messages[2] = "Message Three";

            // set priority and send messages
            producer.setPriority(3);
            producer.send(queue, messages[0]);

            producer.setPriority(1);
            producer.send(queue, messages[1]);

            producer.setPriority(9);
            producer.send(queue, messages[2]);


            // Create Consumer
            JMSConsumer consumer = jmsContext.createConsumer(queue);
            for (int i = 0; i <= 2; i++) {
                TextMessage textMessage = (TextMessage) consumer.receive();
                System.out.println(textMessage.getJMSPriority() + " : " + textMessage.getText());
            }

//            9 : Message Three
//            3 : Message One
//            1 : Message Two

//          Default Priority
//            4 : Message One
//            4 : Message Two
//            4 : Message Three

        }
    }
}
