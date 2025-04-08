<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
	<div id="app">
		<h2>�׽�Ʈ</h2>
		<div v-for="(msg, index) in messages" :key="index">{{msg.sender}}: {{msg.content}}</div>
		<input v-model="inputSender" placeholder="�г��� �Է�">
		<input v-model="message" @keyup.enter="sendMessage">
	</div>
	
	<script>
		let contextPath = '<%= request.getContextPath() %>';
		
		let app = Vue.createApp({
			data() {
				return {
					stompClinet: null,
					messages: [],
					message: '',
					inputSender: ''
				}
			},
			mounted() {
				const socket = new SockJS(contextPath + '/ws');
				this.stompClient = Stomp.over(socket);
				
				this.stompClient.connect({}, () => {
					console.log("STOMP ����");
					
					this.stompClient.subscribe("/topic/messages", (msg) => {
						const body = JSON.parse(msg.body);
						this.messages.push(body);
					});
				});
			},
			methods: {
				sendMessage() {
					if (!this.message || !this.inputSender) {
						return;
					}
					const chatMessage = {
							sender: this.inputSender,
							content: this.message,
							roomId: "default"
					};
					
					this.stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
					this.message = '';
				}
			}
		}).mount('#app');
	</script>
</body>
</html>