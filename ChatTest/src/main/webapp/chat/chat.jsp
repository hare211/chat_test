<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

</head>
<body>
	<div id="app">
		<h2>테스트</h2>
		
		<label>방 선택&nbsp;&nbsp;</label>
		<select v-model="roomId" @change="changeRoom()">
			<option v-for="room in availableRooms" :value="room.roomId">{{room.name}}</option>
		</select>
		<div v-for="(msg, index) in messages" :key="index">{{msg.sender}}: {{msg.content}}</div>
		<!-- <input v-model="inputSender" placeholder="닉네임 입력"> -->
		<input v-model="message" @keyup.enter="sendMessage()">
	</div>
	
	<script>
		let contextPath = '<%= request.getContextPath() %>';
		//const accessToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzQ0MTY0MDMzLCJleHAiOjE3NDQxNjc2MzN9.ePS7l9OH0wX0uq583M5WTKGHqkiZW3agL42-so1q2vI';
		let app = Vue.createApp({
			data() {
				return {
					stompClient: null,
					messages: [],
					message: '',
					userId: '',
					availableRooms: [],
					roomId: '',
					subscription: null
				}
			},
			mounted() {
				this.initialize();
			},
			methods: {
				async initialize() {
					try {
						const res = await axios.get(contextPath + '/api/token');
						const accessToken = res.data;
						console.log(accessToken);
						
						const socket = new SockJS(contextPath + '/ws');
						this.stompClient = Stomp.over(socket);
						
						this.stompClient.connect(
							{ Authorization: 'Bearer ' + accessToken },
							() => {
								console.log('STOMP 연결 성공');
								this.loadRooms();
							},
							(error) => {
								console.error('STOMP 연결 실패', error);
							}
						);
					} catch (err) {
						console.error('토큰 가져오기 실패:', err);
					}
				},
				async loadRooms() {
					try {
						const res = await axios.get(contextPath + '/chat/room');
						this.availableRooms = res.data;
						this.roomId = this.availableRooms[0].roomId; // default room
						this.subscribeRoom();
					} catch (err) {
						console.error('roomId 불러오기 실패: ', err);
					}						 
				},
				subscribeRoom() {
					if (this.subscription) {
						this.subscription.unsubscribe();
					}
					this.messages = []; // /topic/chat/ => 서버 -> 클라이언트
					this.subscription = this.stompClient.subscribe('/topic/chat/' + this.roomId, (msg) => {
						const body = JSON.parse(msg.body);
						this.messages.push(body);
					});
				},
				changeRoom() {
					this.subscribeRoom();
				},
				sendMessage() {
					if (!this.message) {
						return;
					}
					const chatMessage = {
						sender: this.userId,
						content: this.message,
						roomId: this.roomId
					};
					
					if (this.stompClient && this.stompClient.connected) {
						this.stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
						this.message = ''; // /app/chat => 클라이언트 -> 서버
					}
				}
			}
		}).mount('#app');
	</script>
</body>
</html>