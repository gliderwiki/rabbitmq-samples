Hello World - http://www.rabbitmq.com/tutorials/tutorial-one-java.html
- One to one publish / subscribe

C가 1개이므로 작업(메시지 처리)가 분산되지 않아, 실제로 사용할 일은 많지 않다.
다만 로직을 P와 C로 분리하여, 오래 걸리는 작업은 C가 담당하면 사용자 입장에서의 응답속도가 빨라질 것이다.
