package paulevs.bhcore.mixin;

import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = JsonUnbakedModel.class, remap = false)
public interface JsonUnbakedModelAccessor {
	@Accessor("parentId")
	Identifier bhc_getParentID();
	
	@Accessor("parentId")
	void bhc_setParentID(Identifier id);
	
	@Accessor("parent")
	JsonUnbakedModel bhc_getParent();
	
	@Accessor("parent")
	void bhc_setParent(JsonUnbakedModel model);
}
