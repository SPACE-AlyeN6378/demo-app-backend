import React, { Component } from 'react';
// import { over } from 'stompjs';
import SockJS from 'sockjs-client';

var stompClient = null;
class ChatRoom extends Component {
    constructor(props) {
        super(props);

        this.state = {
            user_name: "",
            receiver_name: "",
            connected: false,
            message: ""
        }
    }

    componentDidMount() {
        let name = this.props.app_getName();
        this.setState({user_name: name});
    }


    // registerUser = () => {
    //     let Sock = new SockJS("http://localhost:8080/chat");
    //     stompClient = over(Sock);
    //     stompClient.connect({}, onConnected, onError);
    // }

    // onConnected = () => {
    //     this.setState({connected: true});
    //     stompClient.subscribe('/chatroom/public', onPublicMessageReceived);
    //     stompClient.subscribe("/user/"+this.)
    // }

    
    render() { 
        return (
            <div>
                Hello, {this.state.user_name}!
            </div>
        );
    }
}
 
export default ChatRoom;