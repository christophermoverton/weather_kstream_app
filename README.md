
# Weather Alert Kafka Streams Application

This project is a Kafka Streams application that processes weather alert data in real-time. The application listens to a Kafka topic for weather alert messages, filters them based on severity, and outputs severe weather alerts.

## Prerequisites

Before you begin, ensure that you have the following installed on your local machine:

- **Docker**
- **Docker Compose**
- **Java JDK (11 or later)**
- **Maven**
- **Kafka & Zookeeper (Dockerized)**

This project is designed to be used with the [Dockerized Kafka and Zookeeper setup](https://github.com/christophermoverton/docker_kafka_weatheralert). Be sure to follow the instructions provided in that repository to set up Kafka and Zookeeper services properly.

### Installing Java JDK & Maven

To build and run the application locally before deploying with Docker, you'll need to have Java JDK and Maven installed.

#### Install Java JDK (11 or later)

**Ubuntu/Debian:**

```bash
sudo apt update
sudo apt install openjdk-11-jdk
```

**MacOS (using Homebrew):**

```bash
brew tap AdoptOpenJDK/openjdk
brew install --cask adoptopenjdk11
```

**Windows:**

Download the JDK installer from the [Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) and follow the installation instructions.

Verify the installation:

```bash
java -version
```

#### Install Maven

**Ubuntu/Debian:**

```bash
sudo apt update
sudo apt install maven
```

**MacOS (using Homebrew):**

```bash
brew install maven
```

**Windows:**

Download the Maven binary from the [Apache Maven website](https://maven.apache.org/download.cgi) and follow the installation instructions.

Verify the installation:

```bash
mvn -version
```

## Project Structure

The key components of this project include:

- **Kafka Streams App**: The core of the application that processes incoming Kafka messages.
- **Kafka & Zookeeper**: The services necessary for running the Kafka broker and managing its distributed systems.

## Build & Run the Application

### Step 1: Clone the Repository

First, clone this repository to your local machine:

```bash
git clone https://github.com/christophermoverton/weather_kstream_app.git
cd weather-alert-kafka-streams-app
```

### Step 2: Build the Application Locally

Before deploying the application with Docker, you can build it locally using Maven to ensure everything is set up correctly.

Run the following command in the project root directory:

```bash
mvn clean install
```

This will compile the Java code and package the application into a JAR file located in the `target/` directory.

### Step 3: Kafka & Zookeeper Setup

Ensure that you have Kafka and Zookeeper services already running. If not, you can set them up using the provided Dockerized Kafka and Zookeeper setup [here](https://github.com/christophermoverton/docker_kafka_weatheralert). These services should be configured with the correct network (`kafka-network`) and use the following listener:

```bash
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
```

### Step 4: Build and Run the Dockerized Application

Once you've built the application locally, you can now build and run the Docker containers using Docker Compose.

Run the following command to build the Docker image and start the containers:

```bash
sudo docker-compose up --build
```

This command will:

- Build the Kafka Streams application Docker image.
- Start the Kafka Streams application container and attach it to the correct network.

### Step 5: Verify the Application

Once the application is running, check the logs to verify that it is successfully connecting to the Kafka broker and processing messages.

To view the logs, run:

```bash
sudo docker logs -f weather_alert_app
```

### Step 6: Stopping the Application

To stop the application and all running containers, run:

```bash
sudo docker-compose down
```

This command will stop the containers and remove the associated Docker networks and volumes.

## Environment Variables

The application requires the following environment variables to be set for proper configuration:

- `KAFKA_BOOTSTRAP_SERVERS`: The Kafka broker address in the format `kafka:9092`. This should match the advertised listeners of your Kafka broker.

These are set in the `docker-compose.yml` file:

```yaml
services:
  kafka-streams-app:
    environment:
      - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

Ensure that the Kafka Streams application is connected to the same network as your Kafka broker.

## Example Alert Message Format

The Kafka Streams application processes weather alerts in JSON format. Below is an example of the expected weather alert message:

```json
{
  "id": "https://api.weather.gov/alerts/urn:oid:2.49.0.1.840.0.68042dc6348143bd6983f1abb510867fb122b57c.031.1",
  "type": "Feature",
  "properties": {
    "severity": "Severe",
    "event": "Small Craft Advisory",
    "headline": "Small Craft Advisory issued August 21",
    "description": "SW wind 20 kt. Seas 6 ft."
  }
}
```

The application filters the messages based on the `severity` field. Alerts with a severity of `"Severe"` will be processed.

## Troubleshooting

### Common Issues:

1. **Kafka Streams App Not Connecting to Kafka**:
   - Ensure that the Kafka broker is running and the `KAFKA_ADVERTISED_LISTENERS` is correctly set to `PLAINTEXT://kafka:9092`.
   - Verify that both the Kafka Streams application container and Kafka broker are on the same Docker network (`kafka-network`).

2. **Network Connectivity**:
   - Use `ping kafka` or `nslookup kafka` within the Kafka Streams container to verify that the Kafka broker is reachable.

3. **Logs**:
   - Always check the application logs using `docker logs` for any error messages related to connectivity or processing.

## Contributing

Feel free to open issues or submit pull requests to improve this application.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

