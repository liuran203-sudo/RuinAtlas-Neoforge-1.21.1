package com.liuran.ruinatlas.client;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * 本模组的两个按键：施展契法与灵契轮盘
 */
public final class RaKeyMappings {
	public static final String CATEGORY = "key.categories.ruinatlas";

	public static final KeyMapping CAST = new KeyMapping("key.ruinatlas.cast",
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY);

	public static final KeyMapping WHEEL = new KeyMapping("key.ruinatlas.wheel",
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY);

	private RaKeyMappings() {
	}
}
