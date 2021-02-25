package rip.helium.event.events.callables;

import rip.helium.event.events.Cancellable;
import rip.helium.event.events.Event;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class EventCancellable implements Event, Cancellable {

    private boolean cancelled;

    protected EventCancellable() {
    }

    /**
     * @see rip.helium.event.events.Cancellable.isCancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @see rip.helium.event.events.Cancellable.setCancelled
     */
    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
