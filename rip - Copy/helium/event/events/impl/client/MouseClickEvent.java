package rip.helium.event.events.impl.client;

import rip.helium.event.events.Event;

public class MouseClickEvent implements Event {
    private final int mouseButton;

    public MouseClickEvent(int mouseButton) {
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}
