package org.strangeway.eventrouter.test;

import org.junit.Assert;
import org.junit.Test;
import org.strangeway.eventrouter.EventRouter;
import org.strangeway.eventrouter.test.components.Label;
import org.strangeway.eventrouter.test.components.Label.TextChangeEvent;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Yuriy Artamonov
 */
public class EventRouterTest {
    @Test
    public void addListeners() {
        EventRouter eventRouter = new EventRouter();
        assertFalse(eventRouter.hasListeners(TextChangeEvent.class));

        AtomicInteger counter = new AtomicInteger();
        eventRouter.addListener(TextChangeEvent.class, event ->
                counter.addAndGet(1)
        );

        assertTrue(eventRouter.hasListeners(TextChangeEvent.class));
        eventRouter.fireListeners(TextChangeEvent.class, new TextChangeEvent(new Label()));

        Assert.assertEquals(1, counter.get());
    }
}