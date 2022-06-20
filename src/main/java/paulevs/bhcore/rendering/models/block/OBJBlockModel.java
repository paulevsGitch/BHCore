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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Environment(EnvType.CLIENT)
public class OBJBlockModel implements UnbakedModel, BakedModel {
	private final ImmutableList<SpriteIdentifier> textures;
	private final ModelTransformation transformation;
	private final ImmutableList<QuadInfo> infoQuads;
	private final Identifier particles;
	private final boolean useAO;
	
	private ImmutableList<BakedQuad> bakedQuads;
	private Sprite particleSprite;
	
	private OBJBlockModel(ImmutableList<QuadInfo> infoQuads, ModelTransformation transformation, ImmutableList<SpriteIdentifier> textures, Identifier particle, boolean useAO) {
		this.transformation = transformation;
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
		particleSprite = textureGetter.apply(SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, this.particles));
		bakedQuads = infoQuads.stream().map(quad -> quad.bake(textureGetter)).collect(ImmutableList.toImmutableList());
		return this;
	}
	
	// Baked Model
	
	@Override
	public ImmutableList<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return bakedQuads;
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
		boolean useAO = obj.has("useAO") ? obj.get("useAO").getAsBoolean() : false;
		Vec3F offset = obj.has("offset") ? JsonUtil.vectorFromArray(obj.getAsJsonArray("offset")) : new Vec3F();
		
		Identifier particles;
		SpriteIdentifier[] textures;
		JsonObject packedTextures = obj.has("textures") ? obj.getAsJsonObject("textures") : null;
		
		if (packedTextures != null) {
			textures = new SpriteIdentifier[packedTextures.size() - 1];
			particles = Identifier.of(packedTextures.get("particle").getAsString());
			packedTextures.keySet().stream().filter(name -> name.startsWith("material")).forEach(name -> {
				int index = Integer.parseInt(name.substring(9));
				Identifier id = Identifier.of(packedTextures.get(name).getAsString());
				textures[index] = SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, id);
			});
		}
		else {
			particles = Identifier.of("block/stone");
			textures = new SpriteIdentifier[] { SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, particles) };
		}
		
		LogUtil.log("Textures:", Arrays.toString(textures));
		
		Resource resource = Resource.of(resourceManager.getResourceAsStream(ResourceManager.ASSETS.toPath(
			Identifier.of(objName),
			MODID + "/models",
			"obj"
		)));
		
		List<QuadInfo> quads = new ArrayList<>();
		if (resource != null) {
			loadOBJ(resource.getInputStream(), quads, textures, offset);
			resource.close();
		}
		
		return new OBJBlockModel(ImmutableList.copyOf(quads), ModelTransformation.NONE, ImmutableList.copyOf(textures), particles, useAO);
	}
	
	private static void loadOBJ(InputStream stream, List<QuadInfo> quads, SpriteIdentifier[] textures, Vec3F offset) {
		List<Float> vertexData = new ArrayList<>(12);
		List<Float> uvData = new ArrayList<>(8);
		
		List<Integer> vertexIndex = new ArrayList<>(4);
		List<Integer> uvIndex = new ArrayList<>(4);
		
		Vec2F emptyUV = new Vec2F();
		
		byte maxTextures = (byte) (textures.length - 1);
		byte texIndex = -1;
		
		try {
			InputStreamReader streamReader = new InputStreamReader(stream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			String string;
			
			while ((string = bufferedReader.readLine()) != null) {
				if (string.startsWith("usemtl")) {
					texIndex++;
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
							vertexIndex.add(Integer.parseInt(sub[0]) - 1); // Vertex
							uvIndex.add(Integer.parseInt(sub[1]) - 1);     // UV
						}
						else {
							vertexIndex.add(Integer.parseInt(member) - 1); // Vertex
						}
					}
					
					QuadInfo quad = new QuadInfo();
					quad.setTexture(textures[MathUtil.clamp(texIndex, 0, maxTextures)]);
					quads.add(quad);
					
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
				}
			}
			
			bufferedReader.close();
			streamReader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
