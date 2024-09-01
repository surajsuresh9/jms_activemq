package com_activemq_message_basics;

import com_activemq_model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;

public class MessageTypesDemo {
    public static void main(String[] args) throws Exception {
        InitialContext initialContext = new InitialContext();
        Queue queue = (Queue) initialContext.lookup("queue/myQueue");

        try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext jmsContext = (JMSContext) cf.createContext()) {
//            TextMessage message = jmsContext.createTextMessage("Hello World!");

            // BytesMessage
            BytesMessage bytesMessage = jmsContext.createBytesMessage();
            bytesMessage.writeUTF("John");
            bytesMessage.writeLong(123);

            // StreamMessage
            StreamMessage streamMessage = jmsContext.createStreamMessage();
            streamMessage.writeBoolean(true);
            streamMessage.writeFloat(2.5F);

            // MapMessage
            MapMessage mapMessage = jmsContext.createMapMessage();
            mapMessage.setBoolean("isCreditAvailable", false);

            // MapMessage
            ObjectMessage objectMessage = jmsContext.createObjectMessage();
            objectMessage.setObject(new Patient(123, "John"));


            //            JMSProducer producer = jmsContext.createProducer().send(queue, bytesMessage);
            //            JMSProducer producer = jmsContext.createProducer().send(queue, streamMessage);
//            JMSProducer producer = jmsContext.createProducer().send(queue, mapMessage);
            JMSProducer producer = jmsContext.createProducer().send(queue, objectMessage);
            //            producer.send(queue, bytesMessage);
            //            producer.send(queue, streamMessage);
            producer.send(queue, mapMessage);


            //            BytesMessage receivedBytesMessage = (BytesMessage) jmsContext.createConsumer(queue).receive(2500);
            //            StreamMessage receivedStreamMessage = (StreamMessage) jmsContext.createConsumer(queue).receive(2500);
            //            MapMessage receivedMapMessage = (MapMessage) jmsContext.createConsumer(queue).receive(2500);
            ObjectMessage recievedObjectMessage = (ObjectMessage) jmsContext.createConsumer(queue).receive(2500);
            Patient patient = (Patient) recievedObjectMessage.getObject();


            //            System.out.println(receivedBytesMessage.readUTF());
            //            System.out.println(receivedBytesMessage.readLong());
            //            System.out.println(receivedStreamMessage.readBoolean());
            //            System.out.println("isCreditAvailable: " + receivedMapMessage.getBoolean("isCreditAvailable"));
            System.out.println(patient.getId());
            System.out.println(patient.getName());

//            John          true
//            123           2.5

        }
    }
}
