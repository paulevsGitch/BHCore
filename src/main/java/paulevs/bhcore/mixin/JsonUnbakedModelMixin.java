package paulevs.bhcore.mixin;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.modificationstation.stationapi.api.client.render.model.ItemModelGenerator;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElement;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementFace;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverride;
import net.modificationstation.stationapi.api.client.texture.MissingSprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bhcore.rendering.models.block.OBJBlockModel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

@Mixin(JsonUnbakedModel.class)
public class JsonUnbakedModelMixin {
	@Shadow private Identifier parentId;
	@Shadow @Final private List<ModelOverride> overrides;
	@Shadow public String id;
	
	@Inject(method = "getTextureDependencies(Ljava/util/function/Function;Ljava/util/Set;)Ljava/util/Collection;", at = @At("HEAD"), cancellable = true)
	private void bhc_getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences, CallbackInfoReturnable<Collection<SpriteIdentifier>> info) {
		Set<UnbakedModel> set = Sets.newLinkedHashSet();
		
		for(JsonUnbakedModel model = JsonUnbakedModel.class.cast(this); model != null && JsonUnbakedModelAccessor.class.cast(model).bhc_getParentID() != null && JsonUnbakedModelAccessor.class.cast(model).bhc_getParent() == null; model = JsonUnbakedModelAccessor.class.cast(model).bhc_getParent()) {
			set.add(model);
			UnbakedModel unbakedModel = unbakedModelGetter.apply(JsonUnbakedModelAccessor.class.cast(model).bhc_getParentID());
			
			if (unbakedModel == null) {
				LOGGER.warn("No parent '{}' while loading model '{}'", this.parentId, model);
			}
			
			if (set.contains(unbakedModel)) {
				LOGGER.warn("Found 'parent' loop while loading model '{}' in chain: {} -> {}", model, set.stream().map(Object::toString).collect(Collectors.joining(" -> ")), this.parentId);
				unbakedModel = null;
			}
			
			boolean isOBJ = unbakedModel instanceof OBJBlockModel;
			if (unbakedModel == null || isOBJ) {
				JsonUnbakedModelAccessor.class.cast(model).bhc_setParentID(ModelLoader.MISSING.asIdentifier());
				unbakedModel = unbakedModelGetter.apply(JsonUnbakedModelAccessor.class.cast(model).bhc_getParentID());
			}
			
			if (!isOBJ) {
				if (!(unbakedModel instanceof JsonUnbakedModel jsonModel)) {
					throw new IllegalStateException("BlockModel parent has to be a block model.");
				}
				JsonUnbakedModelAccessor.class.cast(model).bhc_setParent(jsonModel);
			}
		}
		
		Set<SpriteIdentifier> set2 = Sets.newHashSet(this.resolveSprite("particle"));
		
		for (ModelElement modelElement : this.getElements()) {
			SpriteIdentifier spriteIdentifier;
			for (ModelElementFace modelElementFace : modelElement.faces.values()) {
				spriteIdentifier = this.resolveSprite(modelElementFace.textureId);
				if (spriteIdentifier.texture == MissingSprite.getMissingSpriteId()) {
					unresolvedTextureReferences.add(Pair.of(modelElementFace.textureId, this.id));
				}
				set2.add(spriteIdentifier);
			}
		}
		
		this.overrides.forEach((modelOverride) -> {
			UnbakedModel unbakedModel = unbakedModelGetter.apply(modelOverride.getModelId());
			if (!Objects.equals(unbakedModel, this)) {
				set2.addAll(unbakedModel.getTextureDependencies(unbakedModelGetter, unresolvedTextureReferences));
			}
		});
		if (this.getRootModel() == ModelLoader.GENERATION_MARKER) {
			ItemModelGenerator.LAYERS.forEach((string) -> set2.add(this.resolveSprite(string)));
		}
		
		info.setReturnValue(set2);
	}
	
	@Shadow
	public SpriteIdentifier resolveSprite(String spriteName) { return null; }
	
	@Shadow
	public List<ModelElement> getElements() { return null; }
	
	@Shadow
	public JsonUnbakedModel getRootModel() { return null; }
}
