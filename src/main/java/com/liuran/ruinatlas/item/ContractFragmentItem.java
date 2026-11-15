package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 契卷残页：同一种契法的三张残页，可在墟龛合成完整契卷
 * <p>残页本身不能修习，只用自定义数据记着它归属哪一道契法。
 */
public class ContractFragmentItem extends Item {
	public static final String TECHNIQUE_KEY = "technique";

	public ContractFragmentItem(Properties properties) {
		super(properties);
	}

	public static ItemStack create(String techniquePath) {
		ItemStack stack = new ItemStack(com.liuran.ruinatlas.init.RaItems.CONTRACT_FRAGMENT.get());
		setTechnique(stack, techniquePath);
		return stack;
	}

	public static void setTechnique(ItemStack stack, String techniquePath) {
		CompoundTag tag = new CompoundTag();
		tag.putString(TECHNIQUE_KEY, techniquePath);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}

	public static String techniqueOf(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		return data == null ? "" : data.copyTag().getString(TECHNIQUE_KEY);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		String path = techniqueOf(stack);
		RaTechniqueInfo info = path.isEmpty() ? null : RaTechniqueRegistry.byId(path);

		if (info == null) {
			tooltip.add(Component.translatable("item." + MODID + ".contract_fragment.tooltip_unset")
					.withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("item." + MODID + ".contract_fragment.tooltip",
					Component.translatable(info.nameKey())).withStyle(ChatFormatting.GRAY));
		}

		tooltip.add(Component.translatable("item." + MODID + ".contract_fragment.tooltip_detail")
				.withStyle(ChatFormatting.DARK_GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
