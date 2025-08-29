package demo.server;

//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.inject.Produces;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.A2A;
import io.a2a.spec.JSONRPCError;
import io.a2a.spec.UnsupportedOperationError;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Managed;

//@ApplicationScoped
@Configuration
public class AgentExecutorProducer {

    //@Produces
    @Managed
    public AgentExecutor agentExecutor() {
        return new AgentExecutor() {
            @Override
            public void execute(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
                eventQueue.enqueueEvent(A2A.toAgentMessage("Hello World"));
            }

            @Override
            public void cancel(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
                throw new UnsupportedOperationError();
            }
        };
    }
}
