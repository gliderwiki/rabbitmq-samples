AMQP (Advanced Message Queuing Protocol)
- 여러 대의 서버간의 메세지를 주고 받으면서 정보처리하는 것이 목적
- 대량의 메시지를 처리하기 위한 Queueing Protocol
- 이기종간에 메시지를 교환하기 위해서는 메시지 포멧 컨버전을 위한 메시지 브릿지를 이용하거나 (속도 저하 발생) 시스템 자체를 통일시켜야 하는 불편함과 비효율성이 존재하였으나 MQ를 통해 해결

P : Producing. 메시지를 발행
Queue : Producing된 메시지가 저장되는 큐
C : Consuming. 메시지를 받는 측
P는 메시지를 만드는 사람, Q는 우체국, C는 메시지를 받는 사람이다. P가 보낸 메시지는 Queue에 저장되고, 순서대로 C로 보낸다.
이 기본 구성에서 두 개 이상의 C와 Queue를 두거나 RPC 처럼 응답을 받는 식의 응용이 가능하다.

AMQP Routing model
Publisher 가 서버에 메시지를 삽입하면, 서버에서 내부적인 처리를 거쳐 Consumer 에게 전달 , 혹은 Consumer 가 polling 하며 메시지를 획득
- AMQP의 라우팅 모델은 아래와 같은 3개의 중요한 component(Broker) 들로 구성된다.
Exchange
Queue
Binding


- Exchange
  Publisher로부터 수신한 메시지를 적절한 큐 또는 다른 exchange로 분배하는 라우터의 기능을 한다.
  각 큐나 exchange는 'Binding'을 사용해서 exchange에 바인드 되어 있고, 따라서 exchange는 수신한 메시지를 이 binding에 따라 적당한 큐나 exchange로 라우팅한다.
  Binding과 메시지를 매칭시키기 위한 라우팅 알고리즘을 정의한것을 Exchange type이라고 하고, exchange type은 라우팅 알고리즘의 클래스이다.
  브로커는 여러개의 exchange type 인스턴스를 가질 수 있다. binding과 exchange type이 혼동될 수 있는데, exchange type은 메시지를 어떤 방법으로
  라우팅 시킬지를 결정하는 것이고, binding을 이러한 방법을 이용해 실제로 어떤 메시지를 어떤 큐에 보낼지를 결정하는 라우팅 테이블이라고 할 수 있다.
  예를 들어 명함정보가 들어오면 그것을 특정 큐로 보내는 broker가 존재한다면, 명함의 사람 이름 성씨를 보고 큐를 결정하겠다는 것은 exchange type이고, 김씨는 1번큐, 박씨는 2번큐, 이씨는 3번큐로 보내겠다는 것은 binding이다.

- Queue
  일반적으로 알고있는 큐이다. 메모리나 디스크에 메시지를 저장하고, 그것을 consumer에게 전달하는 역할을 한다. 큐는 스스로가 관심있는 메시지 타입을 지정한 Binding을 통해 exchange에 말그대로 bind된다.

- Binding
  exchange와 큐와의 관계를 정의한 일종의 라우팅 테이블이다. 같은 큐가 여러개의 exchange에 bind 될 수도 있고, 하나의 exchange에 여러개의 큐가 binding 될 수도 있다.

- Routing Key
  Publisher에서 송신한 메시지 헤더에 포함되는 것으로 일종의 가상 주소라고 보면 된다. Exchange는 이것을 이용해서 어떤 큐로 메시지를 라우팅할지 결정할 수 있다. (이것을 사용하지 않고 다른 룰을 이용할 수도 있음) AMQP의 표준 exchange type은 이 라우팅 키를 이용하도록 되어있다.


Queue의 주요 속성
name
   durable 속성 : 메세지를 디스크에 저장. memory에 저장하는 것은 transient라고 한다.
   auto-delete : 모든 consumer가 unsubscribe하면, 해당 queue는 자동으로 없어진다.
   * Queue를 만드는 것을 declare라고 하며, 애플리케이션 코드에서도 쉽게 만들 수 있다. 만약 해당 큐가 이미 존재하고 있다면, 다시 queue를 만들지 않고, queue가 없을 경우에만 만든다. (기존의 JMS 기반의 queuing 시스템과 접근 방법이 좀 틀린 듯. 기존 JMS 시스템은 큐 시스템 admin console등에서 queue를 선언했어야 하지만, rabbit mq는 코드를 통해서 queue를 생성 하는 것이 가능하기 때문에, 손쉽게 배포가 가능하다.)


Exchange 타입

Default Exchange
   Direct Exchange : 각 Queue는 Routing Key에 Binding이 되어 있고, Exchange에 Routing Key가 들어오면, 그 Exchange에 Binding되어 있는 Queue중에서,  그 Key와 Mapping되어 있는 Queue로 메세지를 라우팅 한다.
   Fan out Exchange : Routing Key에 상관 없이 Exchange에 Binding되어 있는 모든 Queue에 메세지를 라우팅 한다. (1:N 관계로, 모든 Queue에 메세지를 복제해서 라우팅 한다.)
   Topic Exchange : Exchange에 mapping 되어 있는 Queue중에서 Routing key가 패턴에 맞는 Queue로 모두 메세지를 라우팅 한다.
   Headers Exchange

Binding : Exchange와 Queue를 연결하는 것을 binding이라고 하며, Binding은 routing key와 Exchange type을 attribute로 optional로 동반한다. (routing key는 일종의 filter key 처럼 동작 한다.)
Connection : 물리적인 TCP Connection, 보안이 필요할 경우 TLS(SSL) Connection을 사용할 수 있음.
Channel : 하나의 물리적인 connection 내에 생성되는 가상 논리적인 connection들. Consumer의 process나 thread는 각자 이 channel을 통해서 queue에 연결 될 수 있다.
Virtual Host : 웹서버의 virtual host concept과 같이, 하나의 물리적인 서버에 여러 개의 가상 서버를 만들 수 있다.

메세지 구조 : Header, Properties, byte[] data
트렌젝션 관리 : rabbit mq는 XA 기반의 분산 트렌젝션은 지원하지 않음. local 단위에서 ack 받으면 성공 처리함.