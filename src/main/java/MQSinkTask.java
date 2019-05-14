package com.markcox.kafka.mqsink;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class MQSinkTask extends SinkTask {
  /*
    Your connector should never use System.out for logging. All of your classes should use slf4j
    for logging
 */
  private static Logger log = LoggerFactory.getLogger(MQSinkTask.class);

  Map<String, String> settings;

  MQQueueManager queueManager;
  MQQueue queue;


  MQSinkConnectorConfig config;

  @Override
  public void start(Map<String, String> settings) {
    this.config = new MQSinkConnectorConfig(settings);
    this.settings = settings;

    //TODO: Create resources like database or api connections here.
    MQEnvironment.hostname = settings.get(MQSinkConnectorConfig.MQ_HOSTNAME_CONFIG);
    MQEnvironment.port = Integer.parseInt(settings.get(MQSinkConnectorConfig.MQ_PORT_CONFIG));
    MQEnvironment.channel = settings.get(MQSinkConnectorConfig.MQ_CHANNEL_CONFIG);

    MQEnvironment.properties.put(CMQC.USER_ID_PROPERTY, settings.get(MQSinkConnectorConfig.MQ_USER_CONFIG));
    MQEnvironment.properties.put(CMQC.PASSWORD_PROPERTY, settings.get(MQSinkConnectorConfig.MQ_PASSWORD_CONFIG));
    MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES);


    try {
      queueManager = new MQQueueManager(settings.get(MQSinkConnectorConfig.MQ_QUEUE_MANAGER_CONFIG));

      int openOptions = MQConstants.MQOO_OUTPUT | MQConstants.MQOO_FAIL_IF_QUIESCING;
      queue = queueManager.accessQueue(settings.get(MQSinkConnectorConfig.MQ_QUEUE_CONFIG), openOptions);

    } catch (MQException e) {
      // Do something ...
    }
  }

  @Override
  public void put(Collection<SinkRecord> records) {

    MQPutMessageOptions pmo = new MQPutMessageOptions();

    MQMessage outMsg = new MQMessage();
    outMsg.format = MQConstants.MQFMT_STRING;

    for (SinkRecord record : records) {
      try {
        log.trace("Writing message to QM: {} and Q: {} = {}", settings.get(MQSinkConnectorConfig.MQ_QUEUE_MANAGER_CONFIG),
                settings.get(MQSinkConnectorConfig.MQ_QUEUE_CONFIG),
                record.value().toString());
        outMsg.writeString(record.value().toString());

      } catch(Exception e){
        // Do something ...
      }

      try {
        queue.put(outMsg, pmo);
        queueManager.commit();

      } catch (MQException e) {
        // Do something ...
      }
    }

  }

  @Override
  public void flush(Map<TopicPartition, OffsetAndMetadata> map) {

  }

  @Override
  public void stop() {
    //Close resources here.
    try {
      queue.close();
      queueManager.disconnect();

    } catch (MQException e) {
      // Do something ...
    }
  }

  @Override
  public String version() {
    return AppInfoParser.getVersion();
  }
}
