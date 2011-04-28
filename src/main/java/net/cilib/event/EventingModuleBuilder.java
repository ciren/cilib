package net.cilib.event;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.util.Modules;
import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@code EventingModuleBuilder} is a builder that creates a
 * {@code Module} for a {@code Guice} {@code Injector} instance.
 *
 * <p>
 * Per default, (unless disabled by the user), two events are explicitly
 * captured and processed:
 * <ul>
 * <li>Iteration events for iteration counting</li>
 * <li>Function evaluation events for evaluation counting</li>
 * </ul>
 *
 * Additional {@code Event} monitoring can be configured using the builder
 * methods and then finally creating the {@code Module} by using the
 * {@code build()} method.
 */
public final class EventingModuleBuilder {

    private Map<EventBinding, MethodInterceptor> interceptors = Maps.newHashMap();
    private boolean includeDefaultInterceptors = true;
    private final Module DEFAULT_EVENTS = new AbstractModule() {
        @Override
        protected void configure() {
            requireBinding(Key.get(new TypeLiteral<Queue<Event>>(){}));

            IterationInterceptor iterationInterceptor = new IterationInterceptor();
            FitnessEvaluationInterceptor feval = new FitnessEvaluationInterceptor();
            requestInjection(iterationInterceptor);
            requestInjection(feval);

            bindInterceptor(Matchers.any(), Events.raising(IterationEvent.class), iterationInterceptor);
            bindInterceptor(Matchers.any(), Events.raising(FitnessEvaluationEvent.class), feval);
        }
    };

    public EventingModuleBuilder disableDefaults() {
        includeDefaultInterceptors = false;
        return this;
    }

    public EventingModuleBuilder intercept(Class<?> source, Class<? extends Event> eventType, MethodInterceptor interceptor) {
        interceptors.put(EventBinding.get(source, eventType), interceptor);
        return this;
    }

    public Module build() {
        final Module core = new AbstractModule() {
            @Override
            protected void configure() {
                bind(new TypeLiteral<Queue<Event>>() {}).to(new TypeLiteral<LinkedBlockingQueue<Event>>() {});
            }
        };

        final Module constructed = new AbstractModule() {
            @Override
            protected void configure() {
                install(core);
                for (Map.Entry<EventBinding, MethodInterceptor> entry : interceptors.entrySet()) {
                    bindInterceptor(Events.on(entry.getKey().source),
                            Events.raising(entry.getKey().eventType),
                            entry.getValue());
                }
            }
        };

        return includeDefaultInterceptors ? Modules.combine(core, DEFAULT_EVENTS, constructed) : constructed;
    }

    private static class EventBinding {

        private final Class<?> source;
        private final Class<? extends Event> eventType;

        static EventBinding get(Class<?> source, Class<? extends Event> eventType) {
            return new EventBinding(source, eventType);
        }

        private EventBinding(Class<?> source, Class<? extends Event> eventType) {
            this.source = source;
            this.eventType = eventType;
        }
    }

    public static class IterationInterceptor implements MethodInterceptor {

        @Inject
        private Queue<Event> queue;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("iteration start");
            queue.add(IterationEvent.START);
            Object result = invocation.proceed();
            queue.add(IterationEvent.STOP);
            return result;
        }
    }

    private static class FitnessEvaluationInterceptor implements MethodInterceptor {

        @Inject
        private Queue<Event> eventQueue;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            eventQueue.add(FitnessEvaluationEvent.START);
            Object result = invocation.proceed();
            eventQueue.add(FitnessEvaluationEvent.STOP);
            return result;
        }
    }
}
