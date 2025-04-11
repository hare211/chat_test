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
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

<style type="text/css">
.create-room {
	position: fixed;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.4);
}
.create-room-container {
	position: relative;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 550px;
	background: #fff;
	border-radius: 10px;
	padding: 20px;
	box-sizing: border-box;
}
.content {
	margin-top: 10px;
}
</style>
</head>
<body>
	<div id="app">
		<h2>테스트</h2>
		<div class="create-room" v-show="createCheck">
			<div class="create-room-container">
				그룹이름
				<input type="text" class="form-control" name="room_name" v-model="room_name">
				<div class="create-btn gap-3">
					<button @click="createRoom()" class="btn btn-primary btn-sm">생성</button>
					<button @click="roomOpen()" class="btn btn-secondary btn-sm">취소</button>
				</div>
			</div>
		</div>
		<div class="rooms">
			<input type="button" value="그룹 생성" @click="roomOpen()">
		</div>
		<label>방 선택&nbsp;&nbsp;</label>
		<select v-model="room_id" @change="changeRoom()">
			<option v-for="room in availableRooms" :value="room.room_id">{{room.name}}</option>
		</select>
		<div v-for="(msg, index) in messages" :key="index">{{msg.sender_id}}: {{msg.content}}</div>
		<!-- <input v-model="inputSender" placeholder="닉네임 입력"> -->
		<input v-model="message" @keyup.enter="sendMessage()">
	</div>
	
	<script>
		/*
			TO-DO
			1. 방 목록 출력 - rooms 테이블에서 모든 room_id 조회
			2. 방 구독 - sub_rooms 에 user_id, room_id insert
			3. 구독한 방만 출력(내 그룹 보기) - sub_rooms 에서 user_id 로 조회 후 room_id 반환 받고 rooms 테이블에서 room_id 조회 
		*/
		let contextPath = '<%= request.getContextPath() %>';
		//const accessToken = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzQ0MTY0MDMzLCJleHAiOjE3NDQxNjc2MzN9.ePS7l9OH0wX0uq583M5WTKGHqkiZW3agL42-so1q2vI';
		let app = Vue.createApp({
			data() {
				return {
					stompClient: null,
					messages: [],
					message: '',
					sender_id: '',
					availableRooms: [],
					room_id: '',
					subscription: null,
					createCheck: false,
					room_name: '',
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
						this.room_id = this.availableRooms[0].room_id; // default room
						this.subscribeRoom();
					} catch (err) {
						console.error('room_id 불러오기 실패: ', err);
					}						 
				},
				async loadMessages() {
					try {
						const res = await axios.get(contextPath + '/chat/' + this.room_id + '/messages');
						this.messages = res.data;
						console.log('res.data: ' + JSON.stringify(res.data));
						const sentAt = new Date(res.data[0].sent_at);
						console.log('sent: ' + sentAt.toLocaleString()); // sent: 2025. 4. 10. 오전 10:14:02
					} catch (err) {
						console.error('이전 메시지 불러오기 실패: ', err);
					}
				},
				subscribeRoom() {
					if (this.subscription) {
						this.subscription.unsubscribe();
					}
					this.messages = []; // /topic/chat/ => 서버 -> 클라이언트
					this.loadMessages();
					this.subscription = this.stompClient.subscribe('/topic/chat/' + this.room_id, (msg) => {
						const body = JSON.parse(msg.body);
						this.messages.push(body);
					});
				},
				changeRoom() {
					this.subscribeRoom();
				},
				sendMessage() {
					if (!this.message.trim()) {
						return;
					}
					const chatMessage = {
						sender_id: this.sender_id,
						content: this.message,
						room_id: this.room_id
					};
					
					if (this.stompClient && this.stompClient.connected) {
						this.stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
						this.message = ''; // /app/chat => 클라이언트 -> 서버
					}
				},
				roomOpen() {
					this.createCheck = !this.createCheck;
				},
				async createRoom() {
					try {
						const res = await axios.post(contextPath + '/chat/create', {
							user_id: 'user',
							room_name: this.room_name
						});
						console.log('room_res: ' + res.data);
						alert('그룹 이름: ' + res.data.room_name + '으로 생성되었습니다.');
						this.createCheck = !this.createCheck;
					} catch (err) {
						console.error('요청 실패: ', err);
					}	
				}
			}
		}).mount('#app');
	</script>
</body>
</html>