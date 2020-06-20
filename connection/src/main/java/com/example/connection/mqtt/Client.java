package com.example.connection.mqtt;



import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

public class Client {

    public static final String HOST = "tcp://192.168.0.102:61613";
    //    public static final String TOPIC = "abcd1";
    private String clientId; //随意设置
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "admin";
    private String passWord = "password";

    private ScheduledExecutorService scheduler;

    public Client() {
        this("default");
    }

    public Client(String clientId) {
        this.clientId = clientId;
        connect();
    }

    private void connect() {
        try {
            // host为主机名，clientId即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientId, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调
            client.setCallback(new PushCallback());
//先别删！
//            MqttTopic topic = client.getTopic(TOPIC);
//            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//            options.setWill(topic, "close".getBytes(), 2, true);

            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        subscribe(topic, 1);
    }

    public void subscribe(String topic, int Qos) {
        try {
            int[] arrQos = {Qos};
            String[] arrTopic = {topic};
            client.subscribe(arrTopic, arrQos);
            addTopic(arrTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String[] arrTopic) {
        int[] arrQos = new int[arrTopic.length];
        Arrays.fill(arrQos, 1);
        subscribe(arrTopic, arrQos);

    }

    public void subscribe(String[] arrTopic, int[] arrQos) {
        try {
            client.subscribe(arrTopic, arrQos);
            addTopic(arrTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void addTopic(String[] topic) {
        System.out.println(clientId + " subscribe successfully!");
        for (String s : topic) {
            Server.topicContentMap.put(s, "");
            String idList = Server.topicClientMap.get(s); //可能为null
            idList = (idList == null) ? "" : idList;
            if (!idList.contains(s)) {
                idList += (idList.length() == 0)? clientId : "," + clientId; //如果没有重复订阅
            }
            Server.topicClientMap.put(s, idList);
        }
    }

    public static void main(String[] args) throws MqttException {
        Client client = new Client();
//        client.subscribe("abc");//客户端不关闭，服务端重发消息，客户端可以刷新接收的消息
        String[] topics = {"abc", "def", "ghi"};
        client.subscribe(topics);
    }
}
