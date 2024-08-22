package com.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Properties;

public class WeatherAlertStreamApp {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "weather-alert-stream-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_BOOTSTRAP_SERVERS"));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();
        createWeatherAlertStream(builder);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static void createWeatherAlertStream(StreamsBuilder builder) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Consuming from the "weather-alerts" Kafka topic
        KStream<String, String> weatherAlertsStream = builder.stream("weather-alerts");

        // Filter the stream for only severe weather alerts
        KStream<String, String> severeWeatherAlerts = weatherAlertsStream.filter((key, value) -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(value);
                JsonNode propertiesNode = jsonNode.get("properties");
        
                // Ensure 'properties' and 'severity' fields exist
                if (propertiesNode != null && propertiesNode.has("severity")) {
                    String severity = propertiesNode.get("severity").asText();
                    
                    // Print out the severity for verification
                    System.out.println("Parsed Severity: " + severity);
                    
                    return "Severe".equalsIgnoreCase(severity);
                } else {
                    System.err.println("Missing 'severity' field in message: " + value);
                    return false;
                }
            } catch (Exception e) {
                // Log any parsing errors
                System.err.println("Failed to parse message: " + value + " Error: " + e.getMessage());
                return false;
            }
        });
        

        // Write the filtered severe weather alerts to a new Kafka topic
        severeWeatherAlerts.to("severe-weather-alerts");
    }
}
