Producing and consuming IBM MQ messages

#### Setup IBM MQ server

    docker-compose rm -f && docker-compose up --build ibmmq


#### Startup the app

```bash
./gradlew bootRun
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.5.RELEASE)

2019-05-20 14:53:46.439  INFO 23748 --- [           main] com.mageddo.ibmmq.Application            : Starting Application on NI-94377-3 with PID 23748 (/home/elfreitas/dev/projects/spring-boot-ibm-mq/build/classes/java/main started by elfreitas in /home/elfreitas/dev/projects/spring-boot-ibm-mq)
2019-05-20 14:53:46.441  INFO 23748 --- [           main] com.mageddo.ibmmq.Application            : No active profile set, falling back to default profiles: default
2019-05-20 14:53:47.377  INFO 23748 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'taskScheduler'
2019-05-20 14:53:47.575  INFO 23748 --- [           main] com.mageddo.ibmmq.Application            : Started Application in 1.414 seconds (JVM running for 1.715)
2019-05-20 14:53:49.722  INFO 23748 --- [enerContainer-1] com.mageddo.ibmmq.consumer.PongMDB       : status=pong, msg=
  JMSMessage class: jms_bytes
  JMSType:          null
  JMSDeliveryMode:  2
  JMSDeliveryDelay: 0
  JMSDeliveryTime:  0
  JMSExpiration:    0
  JMSPriority:      4
  JMSMessageID:     ID:414d5120514d312020202020202020202ae9e25c03bf9c23
  JMSTimestamp:     1558374829705
  JMSCorrelationID: null
  JMSDestination:   queue:///PING.EVENT
  JMSReplyTo:       null
  JMSRedelivered:   false
    JMSXAppID: mageddo.ibmmq.Application   
    JMSXDeliveryCount: 1
    JMSXUserID: admin       
    JMS_IBM_Character_Set: UTF-8
    JMS_IBM_Encoding: 273
    JMS_IBM_Format:         
    JMS_IBM_MsgType: 8
    JMS_IBM_PutApplType: 28
    JMS_IBM_PutDate: 20190520
    JMS_IBM_PutTime: 17534970
323031392d30352d32305431343a35333a34392e373033333337
```