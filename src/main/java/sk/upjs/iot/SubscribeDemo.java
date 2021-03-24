package sk.upjs.iot;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

public class SubscribeDemo {

    private static class Callback implements MqttCallback {

        @Override
        public void connectionLost(Throwable throwable) {
            System.out.println("Connection lost");
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            System.out.println(topic + ": " + new String(mqttMessage.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
    }

    public static void main(String[] args) {
        String topic = "upjs/iot/#";
        int qos = 2;
        String broker = "tcp://test.mosquitto.org:1883";
        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            sampleClient.subscribe(topic);
            sampleClient.setCallback(new Callback());

            try (Scanner s = new Scanner(System.in)) {
                s.nextLine();
            }

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }


}
