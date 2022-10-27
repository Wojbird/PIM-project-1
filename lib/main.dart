import 'package:flutter/material.dart';
import 'views/countdown.dart';
import 'views/countup.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Timer/Stoper',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: CountupPage(),
    );
  }
}
