package com.liuran.ruinatlas.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

/**
 * 灵息微光：小而亮的青色光点，正弦脉动并淡出
 */
public class SpiritGlowParticle extends TextureSheetParticle {
	private final float phase;

	public SpiritGlowParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
		super(level, x, y, z);
		this.quadSize = 0.1F + this.random.nextFloat() * 0.08F;
		this.lifetime = 55 + this.random.nextInt(25);
		this.setColor(0.62F, 0.92F, 0.92F);
		this.yd = 0.005 + this.random.nextDouble() * 0.004;
		this.phase = this.random.nextFloat() * (float) Math.PI * 2.0F;
		this.setSprite(sprites.get(this.random));
	}

	@Override
	public void tick() {
		super.tick();
		float pulse = 0.45F + 0.35F * Mth.sin(this.age * 0.15F + this.phase);
		float fade = this.age > this.lifetime - 15 ? (this.lifetime - this.age) / 15.0F : 1.0F;
		this.alpha = pulse * fade * 0.8F;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
}
