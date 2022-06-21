package paulevs.bhcore.rendering.models.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import net.modificationstation.stationapi.api.util.math.Direction.AxisDirection;
import paulevs.bhcore.storage.vector.Vec2F;
import paulevs.bhcore.storage.vector.Vec3F;
import paulevs.bhcore.util.MathUtil;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public final class QuadInfo {
	private final Vec3F[] vertex = new Vec3F[4];
	private final Vec2F[] uv = new Vec2F[4];
	private SpriteIdentifier texture;
	private int rotation;
	private boolean shade;
	private int tintIndex;
	private Vec2F uv1;
	private Vec2F uv2;
	
	public QuadInfo setVertex(int index, Vec3F position) {
		vertex[index] = position;
		return this;
	}
	
	public QuadInfo setUV(int index, Vec2F uv) {
		this.uv[index] = uv;
		return this;
	}
	
	public QuadInfo setTexture(SpriteIdentifier texture) {
		this.texture = texture;
		return this;
	}
	
	public QuadInfo setShade(boolean shade) {
		this.shade = shade;
		return this;
	}
	
	public QuadInfo setTintIndex(int tintIndex) {
		this.tintIndex = tintIndex;
		return this;
	}
	
	public BakedQuad bake(Function<SpriteIdentifier, Sprite> textureGetter) {
		Sprite sprite = textureGetter.apply(texture);
		
		int[] data = new int[32];
		Vec3F normal = getNormal();
		Direction dir = getNormalDirection(normal);
		
		for (byte i = 0; i < 4; i++) {
			pack(data, i, sprite);
		}
		
		return new BakedQuad(data, tintIndex, dir, sprite, shade);
	}
	
	private void pack(int[] data, int index, Sprite sprite) {
		int i = index << 3;
		Vec3F pos = vertex[index];
		Vec2F uv = this.uv[index];
		data[i] = Float.floatToRawIntBits(pos.x);
		data[i | 1] = Float.floatToRawIntBits(pos.y);
		data[i | 2] = Float.floatToRawIntBits(pos.z);
		data[i | 3] = -1;
		data[i | 4] = Float.floatToRawIntBits(sprite.getFrameU(uv.x));
		data[i | 5] = Float.floatToRawIntBits(sprite.getFrameV(uv.y));
	}
	
	public Direction getCullingGroup() {
		Vec3F center = getCenter();
		Direction dir = getCullDirection(vertex[0], center);
		if (dir == null) return null;
		for (byte i = 1; i < 4; i++) {
			Direction dir2 = getCullDirection(vertex[i], center);
			if (dir2 != dir) return null;
		}
		return dir;
	}
	
	private Vec3F getNormal() {
		Vec3F a = vertex[1].clone().subtract(vertex[0]).normalize();
		Vec3F b = vertex[2].clone().subtract(vertex[0]).normalize();
		return a.cross(b).normalize();
	}
	
	private Direction getNormalDirection(Vec3F normal) {
		float ax = MathHelper.abs(normal.x);
		float ay = MathHelper.abs(normal.y);
		float az = MathHelper.abs(normal.z);
		float max = MathUtil.max(ax, ay, az);
		
		Axis axis;
		AxisDirection direction;
		
		if (ax == max) {
			axis = Axis.X;
			direction = normal.x > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
		}
		else if (ay == max) {
			axis = Axis.Y;
			direction = normal.y > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
		}
		else {
			axis = Axis.Z;
			direction = normal.z > 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
		}
		
		return Direction.from(axis, direction);
	}
	
	private Vec3F getCenter() {
		Vec3F center = new Vec3F();
		for (byte i = 0; i < 4; i++) {
			center.add(vertex[i]);
		}
		return center.multiply(0.25F);
	}
	
	private Direction getCullDirection(Vec3F vertex, Vec3F center) {
		Vec3F copy = vertex.clone().lerp(center, 0.01F);
		Direction dir = getNormalDirection(copy.subtract(0.5F));
		float distance = 0;
		switch (dir.axis) {
			case X -> distance = MathHelper.abs(copy.x);
			case Y -> distance = MathHelper.abs(copy.y);
			case Z -> distance = MathHelper.abs(copy.z);
		}
		return distance > 0.49F ? dir : null;
	}
}
