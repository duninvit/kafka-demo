rootProject.name = "kafka-demo"
include("twitter-to-kafka-service")
include("kafka-to-elastic-service")

include("config-server")
include("common-config")
include("common-config-data")

include("kafka")
include("kafka:kafka-model")
include("kafka:kafka-admin")
include("kafka:kafka-producer")
include("kafka:kafka-consumer")
