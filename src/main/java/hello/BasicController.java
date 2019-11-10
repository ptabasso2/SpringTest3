package hello;


import io.opentracing.Tracer;
import io.opentracing.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BasicController {

    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);

    @Autowired
    private Tracer tracer;


    @RequestMapping("/Callme")
    public String service() throws InterruptedException {


        try (Scope scope = tracer.buildSpan("Service").startActive(true)) {

            doSomeStuff(scope, "Hello");
            Thread.sleep(2000L);
            doSomeOtherStuff(scope, "World!");
            logger.info("In Service");

        }
        return "Ok\n";
    }



    private String doSomeStuff(Scope scope, String somestring) throws InterruptedException {
        String astring;
        try (Scope scope1 = tracer.buildSpan("doSomeStuff").asChildOf(scope.span()).startActive(true)) {
            astring = String.format("Hello, %s!", somestring);
            Thread.sleep(250L);
            logger.info("In doSomeStuff()");
        }
        return astring;

    }

    private void doSomeOtherStuff(Scope scope, String somestring) throws InterruptedException {
        try (Scope scope1 = tracer.buildSpan("doSomeOtherStuff").asChildOf(scope.span()).startActive(true)) {
            Thread.sleep(180L);
            logger.info("In doSomeOtherStuff()");
        }
        System.out.println(somestring);
        Thread.sleep(320L);
    }


}
