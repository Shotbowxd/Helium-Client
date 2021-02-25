package rip.helium.event.events.impl.player;

import rip.helium.event.events.callables.EventCancellable;

public class ChatMessageEvent extends EventCancellable {

    String message;

    public ChatMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
