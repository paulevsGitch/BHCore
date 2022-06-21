package paulevs.bhcore.interfaces;

import net.minecraft.entity.BaseParticle;
import net.modificationstation.stationapi.api.registry.Identifier;

public interface CustomParticle {
	Identifier getTextureAtlas();
	
	static CustomParticle cast(BaseParticle particle) {
		return (CustomParticle) particle;
	}
}
