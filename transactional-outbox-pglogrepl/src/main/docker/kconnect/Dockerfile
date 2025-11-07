FROM confluentinc/cp-kafka-connect:7.7.0

RUN confluent-hub install --no-prompt debezium/debezium-connector-postgresql:3.1.2

# Copia plugins custom (seu SMT "HeadersExpandFromEncoded")
# Estrutura esperada:
# ./plugins/headers-expand-smt/headers-expand-smt-1.0.0.jar
# ./plugins/headers-expand-smt/META-INF/services/org.apache.kafka.connect.transforms.Transformation
COPY plugins /kafka/connect/plugins
