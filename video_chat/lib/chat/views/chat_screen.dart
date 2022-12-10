import 'package:flutter/material.dart';
import 'package:video_chat/chat/views/video_call_screen.dart';
import 'package:video_chat/chat/widgets/message_bubble.dart';
import 'package:video_chat/chat/widgets/message_text_field.dart';

class ChatScreen extends StatelessWidget {
  const ChatScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Chat'),
        actions: [
          IconButton(
            onPressed: () => Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => const VideoCallScreen()),
            ),
            icon: Icon(Icons.video_camera_front),
          ),
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView.separated(
              padding: const EdgeInsets.only(bottom: 16),
              reverse: true,
              itemBuilder: (_, index) => _messages[index],
              separatorBuilder: (_, __) => const SizedBox(height: 16),
              itemCount: _messages.length,
            ),
          ),
          const MessageTextField(),
        ],
      ),
    );
  }
}

const _messages = <MessageBubble>[
  MessageBubble(
    profileImageUrl:
        'https://images.unsplash.com/photo-1670481517208-64090dc7470b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1935&q=80',
    message: 'Hello',
    date: 'Dec 10, 9.00AM',
  ),
  MessageBubble(
    message: 'Hello',
    date: 'Dec 10, 9.02AM',
  ),
  MessageBubble(
    profileImageUrl:
        'https://images.unsplash.com/photo-1670481517208-64090dc7470b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1935&q=80',
    message: 'Nice App',
    date: 'Dec 10, 9.03AM',
  ),
  MessageBubble(
    message: 'Yeah',
    date: 'Dec 10, 9.04AM',
  ),
];
