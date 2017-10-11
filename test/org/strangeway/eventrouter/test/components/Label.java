/*
 * Copyright (c) 2016 Yuriy Artamonov.
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

package org.strangeway.eventrouter.test.components;

import org.strangeway.eventrouter.EventRouter;
import org.strangeway.eventrouter.ListenerRegistration;

import java.util.EventObject;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Yuriy Artamonov
 */
public class Label {
    protected String text;

    protected EventRouter eventRouter = new EventRouter();

    public ListenerRegistration addTextChangeListener(Consumer<TextChangeEvent> listener) {
        return eventRouter.addListener(TextChangeEvent.class, listener);
    }

    public boolean removeTextChangeListener(Consumer<TextChangeEvent> listener) {
        return eventRouter.removeListener(TextChangeEvent.class, listener);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!Objects.equals(this.text, text)) {
            this.text = text;

            TextChangeEvent event = new TextChangeEvent(this);
            eventRouter.fireListeners(TextChangeEvent.class, event);
        }
    }

    public static class TextChangeEvent extends EventObject {
        public TextChangeEvent(Label source) {
            super(source);
        }

        @Override
        public Label getSource() {
            return (Label) super.getSource();
        }
    }
}