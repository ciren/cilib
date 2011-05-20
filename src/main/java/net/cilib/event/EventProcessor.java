/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.cilib.event;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

/**
 * @since 0.8
 */
public final class EventProcessor {

    private final List<Processor> processors = Lists.newArrayList();

    @Inject
    public EventProcessor() {
    }

    public void addQueueProcessor(Processor p) {
        processors.add(p);
    }

    public void removeQueueProcessor(Processor p) {
        processors.remove(p);
    }

//    /**
//     * Process any incoming events. The events are propagated to all registered
//     * {@code Processor} instances. Events are discarded once removed from the
//     * queue.
//     */
//    public void processEvents() {
//        Event e = null;
//        try {
//            e = eventQueue.take();
//        } catch (InterruptedException ex) {
//        }
//
//        for (Processor p : processors) {
//            p.process(e);
//        }
//    }

    public void process(Event event) {
        for (Processor p : processors) {
            p.process(event);
        }
    }
}
