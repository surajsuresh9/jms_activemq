package com_1;

import org.w3c.dom.Text;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.function.Consumer;

public class FirstTopic {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Topic topic = (Topic) initialContext.lookup("topic/myTopic");
        ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession();
        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 = session.createConsumer(topic);
        TextMessage textMessage = session.createTextMessage("First Topic Broadcast");
        producer.send(textMessage);

        // consumers are ready to receive the messages
        connection.start();
        TextMessage textMessage1 = (TextMessage) consumer1.receive(5000);
        TextMessage textMessage2 = (TextMessage) consumer2.receive(5000);
        System.out.println("textMessage1 from consumer1: " + textMessage1.getText());
        System.out.println("textMessage2 from consumer2: " + textMessage2.getText());
        connection.close();
        initialContext.close();
    }
}
