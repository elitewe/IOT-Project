package com.example.connection.mqtt;

import com.example.connection.exception.ServiceException;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Title:Server
 * Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * @author chenrl
 * 2016年1月6日下午3:29:28
 */
public class Server {

    private static final String HOST = "tcp://192.168.0.102:61613"; //加入配置文件
    private static final String clientId = "server";

    private MqttClient client;

    private String userName = "admin"; //加入配置文件
    private String passWord = "password";

    public static Map<String, String> topicContentMap = new HashMap<>();
    public static Map<String, String> topicClientMap = new HashMap<>(); //

    private MqttMessage message;

    public Server() throws MqttException {
        // MemoryPersistence设置clientId的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientId, new MemoryPersistence());
        connect();
    }

    public void setContent(String topic, String content) {
        topicContentMap.put(topic, content);
        topicClientMap.put(topic, ""); //同步更新
    }

    public void sendMessage() {
        sendMessage(2, true);
    }

    public void sendMessage(int Qos, boolean isRetained) {
        for (Map.Entry<String, String> entry : topicContentMap.entrySet()) {
            this.message = new MqttMessage();
            this.message.setQos(Qos); //决定消息到达次数
            this.message.setRetained(isRetained); //服务器是否保存消息
            this.message.setPayload(entry.getValue().getBytes()); //发送的实际消息
            this.publish(this.client.getTopic(entry.getKey()), this.message);
        }
    }

    private void publish(MqttTopic topic , MqttMessage message) {
        try {
            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            System.out.println("message is published completely! "
                    + token.isComplete());
        } catch (Exception e) {
            throw new ServiceException("Mqtt服务器连接异常！");
        }

    }

    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException {
        Server server = new Server();
        server.setContent("abc", "123");
        server.setContent("def", "456");
        server.setContent("ghi", "789");
        server.sendMessage();
        System.out.println(server.message.isRetained() + "------ratained状态");
    }
}