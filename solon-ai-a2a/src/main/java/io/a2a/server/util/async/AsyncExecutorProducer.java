package io.a2a.server.util.async;

import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Destroy;
import org.noear.solon.annotation.Managed;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;

//@ApplicationScoped
@Configuration
public class AsyncExecutorProducer {

    private final ExecutorService executor;

    //@PostConstruct
    public AsyncExecutorProducer() {
        executor = Executors.newCachedThreadPool();
    }

    //@PreDestroy
    @Destroy
    public void close() {
        executor.shutdown();
    }

    //@Produces
    @Internal
    public Executor produce() {
        return executor;
    }
}
