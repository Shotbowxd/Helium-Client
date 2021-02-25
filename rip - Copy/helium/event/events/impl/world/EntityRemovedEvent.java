package rip.helium.event.events.impl.world;

import net.minecraft.entity.Entity;
import rip.helium.event.events.callables.EventCancellable;

public class EntityRemovedEvent extends EventCancellable {
	
	private final Entity entity;

    public EntityRemovedEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
	
}
