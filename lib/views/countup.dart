import 'dart:core';

import 'package:first_pim_project/views/countdown.dart';
import 'package:flutter/material.dart';
import '../widgets/round-button.dart';

class CountupPage extends StatefulWidget {
  const CountupPage({Key? key}) : super(key: key);

  @override
  _CountupPageState createState() => _CountupPageState();
}

class _CountupPageState extends State<CountupPage>
    with TickerProviderStateMixin {
  late AnimationController controller;
  late Stopwatch _stopwatch;
  bool isPlaying = false;

  String countTime(int milliseconds) {
    var secs = milliseconds ~/ 1000;
    var hours = (secs ~/ 3600).toString().padLeft(2, '0');
    var minutes = ((secs % 3600) ~/ 60).toString().padLeft(2, '0');
    var seconds = (secs % 60).toString().padLeft(2, '0');
    return "$hours:$minutes:$seconds";
  }

  double progressValue = 1;
  Duration duration = Duration(seconds: 5);

  @override
  void initState() {
    super.initState();
    controller = AnimationController(
      vsync: this,
      duration: Duration(seconds: 60),
    );
    _stopwatch = Stopwatch();
    controller.addListener(() {
      setState(
        () {
          progressValue = controller.value;
        },
      );
    });
  }

  void handleStartStop() {
    if (_stopwatch.isRunning) {
      _stopwatch.stop();
    } else {
      _stopwatch.start();
    }
    setState(() {});
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xfff5fbff),
      body: Column(
        children: [
          Expanded(
            child: Center(
              child: Stack(
                alignment: Alignment.center,
                children: [
                  SizedBox(
                    width: 300,
                    height: 300,
                    child: CircularProgressIndicator(
                      backgroundColor: Colors.grey.shade300,
                      strokeWidth: 6,
                      value: progressValue,
                    ),
                  ),
                  GestureDetector(
                    child: AnimatedBuilder(
                      animation: controller,
                      builder: (context, child) => Text(
                        countTime(_stopwatch.elapsedMilliseconds),
                        style: TextStyle(
                          fontSize: 60,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                GestureDetector(
                  onTap: () {
                    handleStartStop();
                    if (controller.isAnimating == false) {
                      controller.forward();
                      controller.addListener(() {
                        if (controller.isCompleted) {
                          controller.repeat();
                        }
                      });
                      setState(() {
                        isPlaying = true;
                      });
                    } else {
                      controller.stop();
                      setState(
                        () {
                          isPlaying = false;
                        },
                      );
                    }
                  },
                  child: RoundButton(
                    icon: isPlaying ? Icons.pause : Icons.play_arrow,
                  ),
                ),
                GestureDetector(
                  onTap: () {
                    controller.reset();
                    if (_stopwatch.isRunning) {
                      _stopwatch.stop();
                    }
                    _stopwatch.reset();
                    setState(() {
                      isPlaying = false;
                    });
                  },
                  child: RoundButton(
                    icon: Icons.stop,
                  ),
                ),
                GestureDetector(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const CountdownPage()),
                    );
                  },
                  child: RoundButton(
                    icon: Icons.alarm,
                  ),
                )
              ],
            ),
          )
        ],
      ),
    );
  }
}
