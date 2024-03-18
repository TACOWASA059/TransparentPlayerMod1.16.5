package com.github.tacowasa059.transparentplayermod.mixin;

import com.github.tacowasa059.transparentplayermod.capabilities.provider.AlphaValueProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static net.minecraft.client.renderer.entity.LivingRenderer.getPackedOverlay;

// 対象のクラスを指定
@Mixin(LivingRenderer.class)
public abstract class MixinLivingRenderer <T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    @Shadow protected M entityModel;

    protected MixinLivingRenderer(List<LayerRenderer<T, M>> layerRenderers) {
        super(null);
        this.layerRenderers = layerRenderers;
    }

    @Shadow protected float getSwingProgress(T p_77040_1_, float p_77040_2_){return 1.0f;};
    @Shadow
    protected float handleRotationFloat(T p2256231, float p2256233) {return 1.0f;}
    @Shadow
    private void applyRotations(T p2256231, MatrixStack p2256234, float f7, float f, float p2256233) {
    }
    @Shadow
    private void preRenderCallback(T p2256231, MatrixStack p2256234, float p2256233) {
    }
    @Shadow
    private float getOverlayProgress(T p2256231, float p2256233) {return 1.0f;
    }

    @Shadow
    private RenderType func_230496_a_(T p2256231, boolean flag, boolean flag1, boolean flag2) {
        return null;
    }

    @Shadow
    private boolean isVisible(T p2256231) {return true;
    }
    @Shadow protected final List<LayerRenderer<T, M>> layerRenderers;

    // renderメソッドに注入する
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
            at = @At(value = "HEAD"),
    cancellable = true)
    private void injectIntoRenderMethod(T p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_, CallbackInfo ci) {
        LivingRenderer<T,M> livingRenderer=(LivingRenderer<T, M>) (Object)this;
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(p_225623_1_, livingRenderer, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_))) {
            p_225623_4_.push();
            this.entityModel.swingProgress = p_225623_1_.getSwingProgress(p_225623_3_);
            boolean shouldSit = p_225623_1_.isPassenger() && p_225623_1_.getRidingEntity() != null && p_225623_1_.getRidingEntity().shouldRiderSit();
            this.entityModel.isSitting = shouldSit;
            this.entityModel.isChild = p_225623_1_.isChild();
            float f = MathHelper.interpolateAngle(p_225623_3_, p_225623_1_.prevRenderYawOffset, p_225623_1_.renderYawOffset);
            float f1 = MathHelper.interpolateAngle(p_225623_3_, p_225623_1_.prevRotationYawHead, p_225623_1_.rotationYawHead);
            float f2 = f1 - f;
            float f7;
            if (shouldSit && p_225623_1_.getRidingEntity() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)p_225623_1_.getRidingEntity();
                f = MathHelper.interpolateAngle(p_225623_3_, livingentity.prevRenderYawOffset, livingentity.renderYawOffset);
                f2 = f1 - f;
                f7 = MathHelper.wrapDegrees(f2);
                if (f7 < -85.0F) {
                    f7 = -85.0F;
                }

                if (f7 >= 85.0F) {
                    f7 = 85.0F;
                }

                f = f1 - f7;
                if (f7 * f7 > 2500.0F) {
                    f += f7 * 0.2F;
                }

                f2 = f1 - f;
            }
            float f6 = MathHelper.lerp(p_225623_3_, p_225623_1_.prevRotationPitch, p_225623_1_.rotationPitch);
            float f8;
            if (p_225623_1_.getPose() == Pose.SLEEPING) {
                Direction direction = p_225623_1_.getBedDirection();
                if (direction != null) {
                    f8 = p_225623_1_.getEyeHeight(Pose.STANDING) - 0.1F;
                    p_225623_4_.translate((double)((float)(-direction.getXOffset()) * f8), 0.0, (double)((float)(-direction.getZOffset()) * f8));
                }
            }
            f7 = this.handleRotationFloat(p_225623_1_, p_225623_3_);
            this.applyRotations(p_225623_1_, p_225623_4_, f7, f, p_225623_3_);
            p_225623_4_.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(p_225623_1_, p_225623_4_, p_225623_3_);
            p_225623_4_.translate(0.0, -1.5010000467300415, 0.0);
            f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && p_225623_1_.isAlive()) {
                f8 = MathHelper.lerp(p_225623_3_, p_225623_1_.prevLimbSwingAmount, p_225623_1_.limbSwingAmount);
                f5 = p_225623_1_.limbSwing - p_225623_1_.limbSwingAmount * (1.0F - p_225623_3_);
                if (p_225623_1_.isChild()) {
                    f5 *= 3.0F;
                }

                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            this.entityModel.setLivingAnimations(p_225623_1_, f5, f8, p_225623_3_);
            this.entityModel.setRotationAngles(p_225623_1_, f5, f8, f7, f2, f6);
            Minecraft minecraft = Minecraft.getInstance();
            boolean flag = this.isVisible(p_225623_1_);
            boolean flag1 = !flag && !p_225623_1_.isInvisibleToPlayer(minecraft.player);
            boolean flag2 = minecraft.isEntityGlowing(p_225623_1_);
            RenderType rendertype = this.func_230496_a_(p_225623_1_, flag, flag1, flag2);
            if (rendertype != null) {
                IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(rendertype);
                int i = getPackedOverlay(p_225623_1_, this.getOverlayProgress(p_225623_1_, p_225623_3_));
                if(p_225623_1_ instanceof AbstractClientPlayerEntity){
                    AbstractClientPlayerEntity player=(AbstractClientPlayerEntity) p_225623_1_;
                    UUID uuid=player.getUniqueID();
                    PlayerEntity playerEntity= (PlayerEntity) player.getEntity();
                    AtomicReference<Float> value= new AtomicReference<>(1.0f);

                    playerEntity.getCapability(AlphaValueProvider.capability).ifPresent(alphaValue -> {
                        int current = alphaValue.getAlpha();
                        value.set((float) current / 255f);
                    });
                    this.entityModel.render(p_225623_4_, ivertexbuilder, p_225623_6_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : value.get());
                }else{
                    this.entityModel.render(p_225623_4_, ivertexbuilder, p_225623_6_, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0f);
                }

            }
            if (!p_225623_1_.isSpectator()) {
                Iterator var24 = this.layerRenderers.iterator();

                while(var24.hasNext()) {
                    LayerRenderer<T, M> layerrenderer = (LayerRenderer)var24.next();
                    layerrenderer.render(p_225623_4_, p_225623_5_, p_225623_6_, p_225623_1_, f5, f8, p_225623_3_, f7, f2, f6);
                }
            }

            p_225623_4_.pop();

            super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(p_225623_1_, livingRenderer, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_));
        }
        ci.cancel();
    }

}