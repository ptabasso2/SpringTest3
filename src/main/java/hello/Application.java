package hello;


import datadog.opentracing.DDTracer;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;




@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    @ConditionalOnProperty(
            value="tracer.type",
            havingValue = "jaeger",
            matchIfMissing = true)
    public Tracer tracer() {
        io.jaegertracing.Configuration.SamplerConfiguration samplerConfig = io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        io.jaegertracing.Configuration.ReporterConfiguration reporterConfig = io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        Configuration config = new Configuration("nfid-identificazione")
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        return config.getTracer();
    }

    @Bean
    @ConditionalOnProperty(
            value="tracer.type",
            havingValue = "ddtracer")
    public Tracer ddtracer() {
        Tracer tracer = new DDTracer("nfid-identificazione");
        GlobalTracer.register(tracer);
        datadog.trace.api.GlobalTracer.registerIfAbsent((datadog.trace.api.Tracer) tracer);
        return tracer;
    }

    @Bean
    @ConditionalOnProperty(
            value="tracer.type",
            havingValue = "ddjavaagent")
    public Tracer ddjavaagent() {
        Tracer tracer = GlobalTracer.get();
        GlobalTracer.registerIfAbsent(tracer);
        return tracer;
    }





}
