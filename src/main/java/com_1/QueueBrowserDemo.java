package com_1;

import org.w3c.dom.Text;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {
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
            TextMessage textMessage1 = session.createTextMessage("This is a test message -- 1");
            TextMessage textMessage2 = session.createTextMessage("This is a test message -- 2");
            producer.send(textMessage1);
            producer.send(textMessage2);
            System.out.println("Message(s) sent successfully");

            QueueBrowser browser = session.createBrowser(queue);
            Enumeration messagesEnum = browser.getEnumeration();
            while (messagesEnum.hasMoreElements()) {
                TextMessage textMessage = (TextMessage) messagesEnum.nextElement();
                System.out.println("Browsing: " + textMessage.getText());
            }

            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived1 = (TextMessage) consumer.receive(5000);
            System.out.println("messageReceived1: " + messageReceived1.getText());
            TextMessage messageReceived2 = (TextMessage) consumer.receive(5000);
            System.out.println("messageReceived2: " + messageReceived2.getText());

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