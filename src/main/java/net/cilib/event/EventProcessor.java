package net.cilib.event;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @since 0.8
 */
public final class EventProcessor {

    private final List<Processor> processors = Lists.newArrayList();
    private final BlockingQueue<Event> eventQueue;

    @Inject
    public EventProcessor(BlockingQueue<Event> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void addQueueProcessor(Processor p) {
        processors.add(p);
    }

    public void removeQueueProcessor(Processor p) {
        processors.remove(p);
    }

    /**
     * Process any incoming events. The events are propagated to all registered
     * {@code Processor} instances. Events are discarded once removed from the
     * queue.
     */
    public void processEvents() {
        Event e = null;
        try {
            e = eventQueue.take();
        } catch (InterruptedException ex) {
        }

        for (Processor p : processors) {
            p.process(e);
        }
    }
}
