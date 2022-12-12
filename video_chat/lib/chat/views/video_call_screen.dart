import 'package:agora_uikit/agora_uikit.dart';
import 'package:flutter/material.dart';

class VideoCallScreen extends StatefulWidget {
  const VideoCallScreen({Key? key}) : super(key: key);

  @override
  State<VideoCallScreen> createState() => _VideoCallScreenState();
}

class _VideoCallScreenState extends State<VideoCallScreen> {
  final AgoraClient _client = AgoraClient(
    agoraConnectionData: AgoraConnectionData(
      appId: '4d4664737ab5435595d0ae6160b9b8e2',
      channelName: 'flutterApp2',
      tempToken:
          '007eJxTYDi8N6Vyo9dv4zk1v5meK0h129m4+HYJ5SjZHHffl/a0+ocCg0mKiZmZibmxeWKSqYmxqamlaYpBYqqZoZlBkmWSRapRE/P05IZARoYFZmcZGKEQxOdmSMspLSlJLXIsKDBiYAAA7pUg6A==',
    ),
  );

  @override
  void initState() {
    super.initState();
    _initAgora();
  }

  Future<void> _initAgora() async {
    await _client.initialize();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async => true,
      child: Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: true,
          title: const Text('Video Call'),
        ),
        body: SafeArea(
          child: Stack(
            children: [
              AgoraVideoViewer(
                client: _client,
                layoutType: Layout.floating,
                showNumberOfUsers: true,
              ),
              AgoraVideoButtons(
                client: _client,
                enabledButtons: const [
                  BuiltInButtons.toggleCamera,
                  BuiltInButtons.callEnd,
                  BuiltInButtons.toggleMic,
                  BuiltInButtons.switchCamera
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
