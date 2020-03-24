package com.example.test;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer1 {
    public static void main(String[] args) {

//       得到连接工厂
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,"tcp://localhost:61616");
        try {
//            得到连接对象
            Connection connection = connect.createConnection();
//            开启连接
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
//            创建会话
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            要消费消息的名称
            Destination testqueue = session.createQueue("drink");
//创建消费者
            MessageConsumer consumer = session.createConsumer(testqueue);
//            创建监听器不断监听提供者的消息发送情况
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if(message instanceof TextMessage){
                        try {
                            String text = ((TextMessage) message).getText();
                            System.out.println(text+"：：我来给您倒水，我是张三");

                            //session.rollback();
                        } catch (JMSException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();;
        }
    }


}
