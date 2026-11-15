package com.liuran.ruinatlas.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

/**
 * 灵霭：大而极淡的青白雾絮，缓慢上浮、随风漂移，进出场都做渐隐
 */
public class SpiritMistParticle extends TextureSheetParticle {
	public SpiritMistParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
		super(level, x, y, z);
		this.quadSize = 0.9F + this.random.nextFloat() * 1.7F;
		this.lifetime = 240 + this.random.nextInt(240);
		this.setColor(0.78F, 0.92F, 0.95F);
		this.alpha = 0.0F;
		double wind = level.getRandom().nextGaussian() * 0.004;
		this.xd = wind;
		this.zd = level.getRandom().nextGaussian() * 0.004;
		this.yd = 0.004 + level.getRandom().nextGaussian() * 0.002;
		this.setSprite(sprites.get(this.random));
	}

	@Override
	public void tick() {
		super.tick();
		float f = this.age < 40 ? this.age / 40.0F : (this.age > this.lifetime - 60 ? Math.max(0.0F, (this.lifetime - this.age) / 60.0F) : 1.0F);
		this.alpha = 0.14F * f;
		this.xd *= 0.995;
		this.zd *= 0.995;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
}
