package paulevs.bhcore.mixin;

import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import paulevs.bhcore.interfaces.CoreNibbleArray;

@Mixin(value = NibbleArray.class, remap = false)
public class NibbleArrayMixin implements CoreNibbleArray {
	@Shadow @Final private byte[] data;
	
	@Override
	public byte bhc_getByte(int index) {
		return data[index];
	}
	
	@Override
	public int bhc_getLength() {
		return data.length;
	}
}
