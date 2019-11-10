
## Tracer switch example with a Spring Boot application 


### Introduction

This is an example of a Spring Boot application where we use Dependency Injection to switch from one tracer implementation to another.<br>
The goal is to take advantage of the spring framework and define beans for each tracer implementation.<br>
This allows anyone to have access to the Jaeger tracer or Datadog tracer by configuration without having to rebuild the application


### Download and install the application

<pre style="font-size: 12px">
git clone https://github.com/ptabasso2/SpringTest3
</pre>

### Configure the tracer
In the `Application` class, we define three beans along with the `@ConditionalOnProperty` which will be used to identify each of the tracers by it's type (`trace.type` property).<br>

Three types are available:
+ Jaeger
+ ddtracer (This is the Datadog opentracing API (dd-trace-ot))
+ ddjavaagent (Tracer that comes with the java agent)


By setting the value to the desired tracer, the corresponding bean will be picked and auto injected by the spring framework everywhere the `@Autowired` annotation is used inside the application.<br>
This property can be set either inside the `application.properties` file or as an option to the JVM (system property) or environment variable. <br>

Exemple:
<pre style="font-size: 12px">
java -jar build/libs/springtest0-1.0.jar -Dtracer.type=ddtracer
</pre>

Important: Jaeger is the **default type** if nothing is specified in the application.properties file or as system property

### Build and run the application

<pre style="font-size: 12px">
[Build] ./gradlew build
[Run]   java -jar build/libs/springtest0-1.0.jar
</pre>

### Test the application

<pre style="font-size: 12px">
[Run]   curl localhost:8080/Callme
Ok
</pre>

