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

package org.strangeway.eventrouter;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * EventRouter class implementing the non-inheritable event listening model.
 *
 * @author Yuriy Artamonov
 * @since 1.0
 */
public class EventRouter {
    protected static final int EVENTS_MAP_EXPECTED_MAX_SIZE = 4;
    protected static final int EVENTS_LIST_INITIAL_CAPACITY = 2;

    // Map with listener classes and listener lists
    // Lists are created on demand
    private Map<Class, List<Consumer>> events = null;

    public <T> ListenerRegistration addListener(Class<T> eventType, Consumer<T> listener) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (events == null) {
            events = new IdentityHashMap<>(EVENTS_MAP_EXPECTED_MAX_SIZE);
        }

        List<Consumer> listeners = events.computeIfAbsent(eventType,
                eventClass -> new ArrayList<>(EVENTS_LIST_INITIAL_CAPACITY));
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

        return new ListenerRegistrationImpl<>(this, eventType, listener);
    }

    public <T> boolean removeListener(Class<T> eventType, Consumer<T> listener) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }

        if (events != null) {
            List<Consumer> listenersList = events.get(eventType);
            if (listenersList != null) {
                boolean wasRemoved = listenersList.remove(listener);
                if (listenersList.isEmpty()) {
                    events.remove(eventType);
                }
                return wasRemoved;
            }
            return false;
        }
        return false;
    }

    public <T> boolean hasListeners(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }

        return events != null
                && events.get(eventType) != null;
    }

    public <T> void fireListeners(Class<T> eventType, T event) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("event cannot be null");
        }

        if (events != null) {
            @SuppressWarnings("unchecked")
            List<Consumer> listeners = events.get(eventType);
            if (listeners != null) {
                for (Object listenerEntry : listeners.toArray()) {
                    @SuppressWarnings("unchecked")
                    Consumer<T> listener = (Consumer<T>) listenerEntry;
                    listener.accept(event);
                }
            }
        }
    }

    protected static class ListenerRegistrationImpl<T> implements ListenerRegistration {
        protected EventRouter router;
        protected Class<T> eventClass;
        protected Consumer<T> listener;

        public ListenerRegistrationImpl(EventRouter router, Class<T> eventClass, Consumer<T> listener) {
            this.router = router;
            this.eventClass = eventClass;
            this.listener = listener;
        }

        @Override
        public boolean removeListener() {
            return router.removeListener(eventClass, listener);
        }
    }
}