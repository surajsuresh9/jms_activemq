package com_activemq_basics;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;

public class JMSContextDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
             JMSContext jmsContext = (JMSContext) cf.createContext()) {
            jmsContext.createProducer().send(queue, "Hello World!");
            System.out.println(jmsContext.createConsumer(queue).receiveBody(String.class));
        }
    }
}
