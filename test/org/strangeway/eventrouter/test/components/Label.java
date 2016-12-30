package org.strangeway.eventrouter.test.components;

import org.strangeway.eventrouter.EventRouter;
import org.strangeway.eventrouter.ListenerRegistration;

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * @author Yuriy Artamonov
 */
public class Label {
    protected EventRouter eventRouter = new EventRouter();

    public ListenerRegistration addTextChangeListener(Consumer<TextChangeEvent> listener) {
        return eventRouter.addListener(TextChangeEvent.class, listener);
    }

    public boolean removeTextChangeListener(Consumer<TextChangeEvent> listener) {
        return eventRouter.removeListener(TextChangeEvent.class, listener);
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