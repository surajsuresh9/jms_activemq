package com_activemq_basics;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstQueue {
    public static void main(String[] args) {
        InitialContext initialContext = null;
        ConnectionFactory connectionFactory;
        Connection connection = null;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");
            MessageProducer producer = session.createProducer(queue);
            TextMessage textMessage = session.createTextMessage("This is a test message");
            producer.send(textMessage);
            System.out.println("Message sent successfully");
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) consumer.receive(5000);
            System.out.println("messageReceived: " + messageReceived.getText());
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        } finally {

            if (initialContext != null) {
                try {
                    initialContext.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}