rootProject.name = "kafka-demo"
include("twitter-to-kafka-service")

include("common-config")
include("common-config-data")

include("kafka")
include("kafka:kafka-model")
include("kafka:kafka-admin")
include("kafka:kafka-producer")
