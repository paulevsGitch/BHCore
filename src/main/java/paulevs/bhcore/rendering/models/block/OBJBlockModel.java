package paulevs.bhcore.rendering.models.block;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.render.model.ModelBakeSettings;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.resource.Resource;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import paulevs.bhcore.storage.vector.Vec2F;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.bhcore.util.JsonUtil;
import paulevs.bhcore.util.LogUtil;
import paulevs.bhcore.util.MathUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Environment(EnvType.CLIENT)
public class OBJBlockModel implements UnbakedModel, BakedModel {
	private final Map<Direction, ImmutableList<QuadInfo>> infoQuads;
	private final ImmutableList<SpriteIdentifier> textures;
	private final ImmutableList<QuadInfo> noCullInfoQuads;
	private final ModelTransformation transformation;
	private final SpriteIdentifier particles;
	private final boolean useAO;
	
	private Map<Direction, ImmutableList<BakedQuad>> bakedQuads = new HashMap<>();
	private ImmutableList<BakedQuad> noCullBakedQuads;
	private Sprite particleSprite;
	
	private OBJBlockModel(Map<Direction, ImmutableList<QuadInfo>> infoQuads, ImmutableList<QuadInfo> noCullInfoQuads, ModelTransformation transformation, ImmutableList<SpriteIdentifier> textures, SpriteIdentifier particle, boolean useAO) {
		this.transformation = transformation;
		this.noCullInfoQuads = noCullInfoQuads;
		this.infoQuads = infoQuads;
		this.particles = particle;
		this.textures = textures;
		this.useAO = useAO;
	}
	
	// Unbaked Model
	
	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return textures;
	}
	
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		particleSprite = textureGetter.apply(this.particles);
		infoQuads.forEach((direction, values) -> bakedQuads.put(direction, bake(values, textureGetter)));
		noCullBakedQuads = bake(noCullInfoQuads, textureGetter);
		for (Direction dir: MathUtil.DIRECTIONS) bakedQuads.computeIfAbsent(dir, key -> ImmutableList.of());
		return this;
	}
	
	private ImmutableList<BakedQuad> bake(List<QuadInfo> list, Function<SpriteIdentifier, Sprite> textureGetter) {
		return list
			.stream()
			.map(quad -> quad.bake(textureGetter))
			.collect(ImmutableList.toImmutableList());
	}
	
	// Baked Model
	
	@Override
	public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return face == null ? noCullBakedQuads : bakedQuads.get(face);
	}
	
	@Override
	public boolean useAmbientOcclusion() {
		return useAO;
	}
	
	@Override
	public boolean hasDepth() {
		return false;
	}
	
	@Override
	public boolean isSideLit() {
		return false;
	}
	
	@Override
	public boolean isBuiltin() {
		return false;
	}
	
	@Override
	public Sprite getSprite() {
		return particleSprite;
	}
	
	@Override
	public ModelTransformation getTransformation() {
		return transformation;
	}
	
	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}
	
	@Override
	public boolean isVanillaAdapter() {
		return true;
	}
	
	public static OBJBlockModel load(TexturePack resourceManager, JsonObject obj) throws IOException {
		String objName = obj.get("linkedOBJ").getAsString();
		
		Resource resource = Resource.of(resourceManager.getResourceAsStream(ResourceManager.ASSETS.toPath(
			Identifier.of(objName),
			MODID + "/models",
			"obj"
		)));
		
		if (resource == null) {
			LogUtil.warn("Specified OBJ model \"" + obj + "\" is missing!");
			return null;
		}
		
		boolean useAO = obj.has("useAO") ? obj.get("useAO").getAsBoolean() : false;
		Vec3F offset = obj.has("offset") ? JsonUtil.vectorFromArray(obj.getAsJsonArray("offset")) : new Vec3F();
		
		List<SpriteIdentifier> sprites = new ArrayList<>();
		JsonObject preMaterials = obj.getAsJsonObject("materials");
		Map<String, MaterialInfo> materials = new HashMap<>();
		preMaterials.keySet().forEach(name -> {
			JsonObject entry = preMaterials.get(name).getAsJsonObject();
			String texture = entry.get("texture").getAsString();
			boolean shade = entry.has("shade") ? entry.get("shade").getAsBoolean() : false;
			int tintIndex = entry.has("tintIndex") ? entry.get("tintIndex").getAsInt() : -1;
			
			JsonObject preCull = entry.has("culling") ? entry.get("culling").getAsJsonObject() : new JsonObject();
			Map<Direction, Boolean> cullingMap = new HashMap<>();
			for (Direction direction: MathUtil.DIRECTIONS) {
				String dirName = direction.toString();
				cullingMap.put(direction, preCull.has(dirName) ? preCull.get(dirName).getAsBoolean() : false);
			}
			
			SpriteIdentifier sprite = SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, Identifier.of(texture));
			materials.put(name, new MaterialInfo(sprite, cullingMap, shade, tintIndex));
			sprites.add(sprite);
		});
		
		Map<Direction, List<QuadInfo>> quads = new HashMap<>();
		List<QuadInfo> noCullQuads = new ArrayList<>();
		if (resource != null) {
			loadOBJ(resource.getInputStream(), quads, noCullQuads, materials, offset);
			resource.close();
		}
		
		Map<Direction, ImmutableList<QuadInfo>> finalQuads = new HashMap<>();
		quads.forEach((dir, group) -> finalQuads.put(dir, ImmutableList.copyOf(group)));
		for (Direction direction: MathUtil.DIRECTIONS) finalQuads.computeIfAbsent(direction, key -> ImmutableList.of());
		
		MaterialInfo preParticle = materials.get("particle");
		SpriteIdentifier particle = preParticle == null ? SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, Identifier.of("missingno")) : preParticle.texture;
		
		return new OBJBlockModel(
			finalQuads,
			ImmutableList.copyOf(noCullQuads),
			ModelTransformation.NONE,
			ImmutableList.copyOf(sprites),
			particle,
			useAO
		);
	}
	
	private static void loadOBJ(InputStream stream, Map<Direction, List<QuadInfo>> quads, List<QuadInfo> noCullQuads, Map<String, MaterialInfo> materials, Vec3F offset) {
		List<Float> vertexData = new ArrayList<>(12);
		List<Float> uvData = new ArrayList<>(8);
		
		List<Integer> vertexIndex = new ArrayList<>(4);
		List<Integer> uvIndex = new ArrayList<>(4);
		
		Vec2F emptyUV = new Vec2F();
		
		try {
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			String string;
			
			MaterialInfo activeMaterial = materials.values().stream().findAny().get();
			while ((string = bufferedReader.readLine()) != null) {
				if (string.startsWith("usemtl")) {
					String materialName = string.substring(7);
					activeMaterial = materials.get(materialName);
				}
				else if (string.startsWith("vt")) {
					String[] uv = string.split(" ");
					uvData.add(Float.parseFloat(uv[1]));
					uvData.add(Float.parseFloat(uv[2]));
				}
				else if (string.startsWith("v")) {
					String[] vert = string.split(" ");
					for (int i = 1; i < 4; i++) {
						vertexData.add(Float.parseFloat(vert[i]));
					}
				}
				else if (string.startsWith("f")) {
					String[] members = string.split(" ");
					if (members.length != 5) {
						throw new RuntimeException("Only quads in OBJ models are supported!");
					}
					vertexIndex.clear();
					uvIndex.clear();
					
					for (int i = 1; i < members.length; i++) {
						String member = members[i];
						
						if (member.contains("/")) {
							String[] sub = member.split("/");
							vertexIndex.add(Integer.parseInt(sub[0]) - 1);
							uvIndex.add(Integer.parseInt(sub[1]) - 1);
						}
						else {
							vertexIndex.add(Integer.parseInt(member) - 1);
						}
					}
					
					QuadInfo quad = new QuadInfo();
					quad.setTexture(activeMaterial.texture);
					quad.setShade(activeMaterial.shade);
					quad.setTintIndex(activeMaterial.tintIndex);
					
					boolean hasUV = !uvIndex.isEmpty();
					for (byte i = 0; i < 4; i++) {
						int index = vertexIndex.get(i) * 3;
						Vec3F vertex = new Vec3F();
						vertex.x = vertexData.get(index++);
						vertex.y = vertexData.get(index++);
						vertex.z = vertexData.get(index++);
						quad.setVertex(i, vertex.add(offset));
						if (hasUV) {
							index = uvIndex.get(i) << 1;
							Vec2F uv = new Vec2F();
							uv.x = uvData.get(index++) * 16.0F;
							uv.y = (1.0F - uvData.get(index++)) * 16.0F;
							quad.setUV(i, uv);
						}
						else {
							quad.setUV(i, emptyUV);
						}
					}
					
					Direction dir = quad.getCullingGroup();
					if (dir != null && activeMaterial.culling.get(dir)) {
						quads.computeIfAbsent(dir, key -> new ArrayList<>()).add(quad);
					}
					else {
						noCullQuads.add(quad);
					}
				}
			}
			
			bufferedReader.close();
			streamReader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private record MaterialInfo(SpriteIdentifier texture, Map<Direction, Boolean> culling, boolean shade, int tintIndex) {}
}
