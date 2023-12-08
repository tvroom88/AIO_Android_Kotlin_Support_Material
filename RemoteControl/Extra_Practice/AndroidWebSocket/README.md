# AndroidWebSocket Example

안드로이드 Okhttp3 라이브러리를 이용한 WebSocket입니다.

server_helper는 node.js 서버 역할을 합니다.

<br/>

node_modules가 없을 경우
1. npm init
2. npm install websocket
3. npm install nodemon --include=dev
4. node.js  실행  : npm start

WebSocket에 url 넣는 부분:
(1) 만약 안드로이드 에뮬레이터를 사용할 것이라면 "ws://10.0.2.2:3000" 
(2) 개인 스마트폰으로 진행한다면 (윈도우) 터미널에서 'ipconfig' 을 쳐서 개인의 ethernet ipv4을 찾기 예) "ws://192.168.200.17:3000"

설명 : https://from-android-to-server.tistory.com/92


