## _spray_ Template Project

This projects provides a starting point for your own _spray-routing_ endeavors.
There are 8 branches, providing templates for _spray-routing_ on

* _spray-can_, Scala 2.9 + Akka 2.0 + spray 1.0 (the `on_spray-can_1.0` branch)
* _spray-can_, Scala 2.10 + Akka 2.1 + spray 1.1 (the `on_spray-can_1.1` branch)
* _spray-can_, Scala 2.10 + Akka 2.2 + spray 1.2 (the `on_spray-can_1.2` branch)
* _spray-can_, Scala 2.10 + Akka 2.3 + spray 1.3 (the `on_spray-can_1.3` branch)
* _spray-can_, Scala 2.11 + Akka 2.3 + spray 1.3 (the `on_spray-can_1.3_scala-2.11` branch)
* _Jetty_, Scala 2.9 + Akka 2.0 + spray 1.0 (the `on_jetty_1.0` branch)
* _Jetty_, Scala 2.10 + Akka 2.1 + spray 1.1 (the `on_jetty_1.1` branch)
* _Jetty_, Scala 2.10 + Akka 2.2 + spray 1.2 (the `on_jetty_1.2` branch)
* _Jetty_, Scala 2.10 + Akka 2.3 + spray 1.3 (the `on_jetty_1.3` branch)
* _Jetty_, Scala 2.11 + Akka 2.3 + spray 1.3 (the `on_jetty_1.3_scala-2.11` branch)

You are currently on the `on_spray-can_1.3_scala-2.11` branch.

Follow these steps to get started:

1. Git-clone this repository.

        $ git clone git://github.com/spray/spray-template.git my-project

2. Change directory into your clone:

        $ cd my-project

3. Launch SBT:

        $ sbt

4. Compile everything and run all tests:

        > test

5. Start the application:

        > re-start

6. Browse to [http://localhost:8080](http://localhost:8080/)

7. Stop the application:

        > re-stop

8. Learn more at http://www.spray.io/

9. Start hacking on `src/main/scala/com/example/MyService.scala`



10 . to test this, use 

curl -v -X POST http://localhost:8881/entity -H "Content-Type: application/json" -d "{ \"cpyBookName\": \"RPWACT\", \"cpyBookHdfsPath\": \"/user/root/LRPWSACT.cpy\", \"fileStructure\":\"FixedLength\", \"binaryFormat\": \"FMT_MAINFRAME\", \"splitOptoin\": \"SplitNone\", \"dataFileHdfsPath\":\"/user/root/RPWACT.FIXED.END\", \"cpybookFont\":\"cp037\" }"
curl -v -X POST http://localhost:8881/entity -H "Content-Type: application/json" -d "{ \"sql\":\"select * from rpwact_temp_1\"}"
curl -v -X POST http://localhost:8881/app -H "Content-Type: application/json" -d "{ \"hdfsPaths\":\"/user/root/TestApp-1.0-SNAPSHOT.jar\"}"
curl -v -X POST http://localhost:8881/app -H "Content-Type: application/json" -d "{ \"userClassName\":\"user.TestApp\"}"
curl -v -X POST http://localhost:8881/app -H "Content-Type: application/json" -d "{ \"srcHdfsPath\":\"/user/root/TestApp.scala\"}"


{"sql":"select * from ATMCFXE_temp_1","conn":"my-spark-app"}

{ "cpyBookName": "ATMCFXE", "cpyBookHdfsPath": "/user/cloudera/ATMCFXE.cpy", "fileStructure":"FixedLength", "binaryFormat": "FMT_MAINFRAME", "splitOptoin": "SplitNone", "dataFileHdfsPath":"/user/cloudera/sampleFXE.VB.RDW.v3.bin", "cpybookFont":"cp037","conn":"my-spark-app" }


{"format":"cpybook",
"cpyBookName": "RPWACT",
"cpyBookHdfsPath": "/user/root/LRPWSACT.cpy",
"fileStructure":"FixedLength",
"binaryFormat": "FMT_MAINFRAME",
"splitOptoin": "SplitNone",
"dataFileHdfsPath":"/user/root/RPWACT.FIXED.END",
"cpybookFont":"cp037"
}
