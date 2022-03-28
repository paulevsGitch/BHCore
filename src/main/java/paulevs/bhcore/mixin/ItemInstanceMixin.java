package paulevs.bhcore.mixin;

import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bhcore.interfaces.CoreItemStack;
import paulevs.bhcore.util.BNTUtil;

@Mixin(ItemInstance.class)
public class ItemInstanceMixin implements CoreItemStack {
	@Unique private static final String NBT_KEY = "bhcore:bnt";
	@Unique private CompoundTag bhc_itemNBT;
	@Shadow public int count;
	@Shadow public int itemId;
	@Shadow private int damage;
	
	@Inject(method = "toTag", at = @At("HEAD"))
	private void bhc_saveToTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info) {
		if (bhc_itemNBT != null) {
			tag.put(NBT_KEY, bhc_itemNBT);
		}
	}
	
	@Inject(method = "fromTag", at = @At("HEAD"))
	private void bhc_loadFromTag(CompoundTag tag, CallbackInfo info) {
		if (tag.containsKey(NBT_KEY)) {
			bhc_itemNBT = tag.getCompoundTag(NBT_KEY);
		}
	}
	
	@Inject(method = "copy()Lnet/minecraft/item/ItemInstance;", at = @At("HEAD"), cancellable = true)
	private void bhc_copyItem(CallbackInfoReturnable<ItemInstance> info) {
		ItemInstance instance = new ItemInstance(this.itemId, this.count, this.damage);
		CoreItemStack self = CoreItemStack.class.cast(this);
		CompoundTag tag = self.bhc_getItemNBT();
		if (tag != null) {
			tag = BNTUtil.copyTag(tag);
			CoreItemStack.class.cast(instance).bhc_setItemNBT(tag);
		}
		info.setReturnValue(instance);
	}
	
	@Inject(method = "isStackIdentical(Lnet/minecraft/item/ItemInstance;)Z", at = @At("HEAD"), cancellable = true)
	private void bhc_isStackIdentical(ItemInstance arg, CallbackInfoReturnable<Boolean> info) {
		if (!bhc_isTagIdentical(arg)) info.setReturnValue(false);
	}
	
	@Inject(method = "isDamageAndIDIdentical(Lnet/minecraft/item/ItemInstance;)Z", at = @At("HEAD"), cancellable = true)
	private void bhc_isDamageAndIDIdentical(ItemInstance arg, CallbackInfoReturnable<Boolean> info) {
		if (!bhc_isTagIdentical(arg)) info.setReturnValue(false);
	}
	
	@Inject(method = "isStackIdentical2(Lnet/minecraft/item/ItemInstance;)Z", at = @At("HEAD"), cancellable = true)
	private void bhc_isStackIdentical2(ItemInstance arg, CallbackInfoReturnable<Boolean> info) {
		if (!bhc_isTagIdentical(arg)) info.setReturnValue(false);
	}
	
	@Unique
	@Override
	@Nullable
	public CompoundTag bhc_getItemNBT() {
		return bhc_itemNBT;
	}
	
	@Unique
	@Override
	public void bhc_setItemNBT(CompoundTag tag) {
		bhc_itemNBT = tag;
	}
	
	@Unique
	private boolean bhc_isTagIdentical(ItemInstance arg) {
		CompoundTag selfTag = CoreItemStack.class.cast(this).bhc_getItemNBT();
		CompoundTag sideTag = CoreItemStack.class.cast(arg).bhc_getItemNBT();
		return BNTUtil.equal(selfTag, sideTag);
	}
}
