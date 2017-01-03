/*
 * Copyright (c) 2016 strangeway.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.strangeway.eventrouter.test;

import org.junit.Test;
import org.strangeway.eventrouter.EventRouter;
import org.strangeway.eventrouter.ListenerRegistration;
import org.strangeway.eventrouter.test.components.Label;
import org.strangeway.eventrouter.test.components.Label.TextChangeEvent;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

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
        eventRouter.fireListeners(TextChangeEvent.class, new CustomTextChangeEvent(new Label()));

        assertEquals(1, counter.get());
    }

    @Test
    public void component() {
        Label label = new Label();

        AtomicInteger counter = new AtomicInteger();
        ListenerRegistration registration = label.addTextChangeListener(event ->
                counter.addAndGet(1)
        );

        label.setText("!");

        assertEquals(1, counter.get());

        registration.removeListener();

        label.setText("@");

        assertEquals(1, counter.get());
    }

    protected static class CustomTextChangeEvent extends TextChangeEvent {
        public CustomTextChangeEvent(Label source) {
            super(source);
        }
    }

    protected static class ExternalListener {

        public void onTextChange() {
        }
    }
}