package io.a2a.solon.integration;

import io.a2a.server.ExtendedAgentCard;
import io.a2a.server.PublicAgentCard;
import io.a2a.server.ServerCallContext;
import io.a2a.server.util.async.Internal;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 *
 * @author noear 2025/8/28 created
 *
 */
public class A2ASolonPlugin implements Plugin {
    private AppContext internalContext = new AppContext();

    @Override
    public void start(AppContext context) throws Throwable {
        context.beanInjectorAdd(PublicAgentCard.class, (vh, anno) -> {
            vh.context().getBeanAsync(vh.getType(), bean -> {
                vh.setValue(bean);
            });
        });

        context.beanExtractorAdd(PublicAgentCard.class, (bw, method, anno) -> {
            bw.context().tryBuildBeanOfMethod(method, bw, (mw, bean) -> {
                bw.context().wrapAndPut(method.getReturnType(), bean);
            });
        });

        context.beanInjectorAdd(ExtendedAgentCard.class, (vh, anno) -> {
            vh.context().getBeanAsync(vh.getType(), bean -> {
                vh.setValue(bean);
            });
        });


        /// ////////

        context.beanInjectorAdd(Internal.class, (vh, anno) -> {
            internalContext.getBeanAsync(vh.getType(), bean -> {
                vh.setValue(bean);
            });
        });

        context.beanExtractorAdd(Internal.class, (bw, method, anno) -> {
            bw.context().tryBuildBeanOfMethod(method, bw, (mw, bean) -> {
                internalContext.wrapAndPut(method.getReturnType(), bean);
            });
        });

        /// ////////

        context.beanScan(ServerCallContext.class);
    }

    @Override
    public void prestop() throws Throwable {
        internalContext.prestop();
    }

    @Override
    public void stop() throws Throwable {
        internalContext.stop();
    }
}