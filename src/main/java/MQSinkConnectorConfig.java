package com.markcox.kafka.mqsink;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;

import java.util.Map;


public class MQSinkConnectorConfig extends AbstractConfig {

  public static final String MQ_HOSTNAME_CONFIG = "mq.hostname";
  private static final String MQ_HOSTNAME_DOC = "";
  public static final String MQ_CHANNEL_CONFIG = "mq.channel";
  private static final String MQ_CHANNEL_DOC = "";
  public static final String MQ_PORT_CONFIG = "mq.port";
  private static final String MQ_PORT_DOC = "";
  public static final String MQ_QUEUE_MANAGER_CONFIG = "mq.queue.manager";
  private static final String MQ_QUEUE_MANAGER_DOC = "";

  public static final String MQ_USER_CONFIG = "mq.user";
  private static final String MQ_USER_DOC = "User ID that the Client will use to connect to MQ.";
  public static final String MQ_PASSWORD_CONFIG = "mq.password";
  private static final String MQ_PASSWORD_DOC = "Password that the Client will use to connect to MQ.";
  public static final String MQ_QUEUE_CONFIG = "mq.queue";
  private static final String MQ_QUEUE_DOC = "Queue that the Client will use to connect to Sink the events.";

  public MQSinkConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
    super(config, parsedConfig);
  }

  public MQSinkConnectorConfig(Map<String, String> parsedConfig) {
    this(config(), parsedConfig);
  }


  public static ConfigDef config() {
    return new ConfigDef()
            .define(MQ_HOSTNAME_CONFIG, Type.STRING, "", Importance.HIGH, MQ_HOSTNAME_DOC)
            .define(MQ_CHANNEL_CONFIG, Type.STRING, "", Importance.HIGH, MQ_CHANNEL_DOC)
            .define(MQ_PORT_CONFIG, Type.INT, 1414, Importance.HIGH, MQ_PORT_DOC)
            .define(MQ_QUEUE_MANAGER_CONFIG, Type.STRING, "QM", Importance.HIGH, MQ_QUEUE_MANAGER_DOC)
            .define(MQ_USER_CONFIG, Type.STRING, "", Importance.HIGH, MQ_USER_DOC)
            .define(MQ_PASSWORD_CONFIG, Type.STRING, "",Importance.HIGH, MQ_PASSWORD_DOC)
            .define(MQ_QUEUE_CONFIG, Type.STRING, "", Importance.HIGH, MQ_QUEUE_DOC)
            ;

  }
}
