package com.example.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class Producer {
    public static void main(String[] args) {
        //得到连接工厂
        ConnectionFactory connect = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            //y由连接工厂创建连接对象
            Connection connection = connect.createConnection();
            //开启连接
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0，由连接得到会话
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            //创建队列模式消息
            Queue testqueue = session.createQueue("drink");
            //创建消息提供者
            MessageProducer producer = session.createProducer(testqueue);
            //定义消息内容
            TextMessage textMessage=new ActiveMQTextMessage();
            textMessage.setText("我想喝水，谁来倒杯水！");
            //持久化消息
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //发送消息
            producer.send(textMessage);
            //关闭会话，关闭连接
            session.commit();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


}
