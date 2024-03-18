package com.github.tacowasa059.transparentplayermod.capabilities.entity;


import net.minecraft.nbt.CompoundNBT;

public class AlphaValue implements IAlphaValue{
        private int alpha = 255;
        @Override
        public void setAlpha(int alpha) {
            if(alpha>255)this.alpha=255;
            else if(alpha<0)this.alpha=0;
            else this.alpha = alpha;
        }
        @Override
        public int getAlpha() {
            return alpha;
        }
        @Override
        public void increaseAlpha(int alpha) {
            this.alpha += alpha;
            if (this.alpha > 255) {
                this.alpha = 255;
            }
        }
        @Override
        public void decreaseAlpha(int alpha) {
            this.alpha -= alpha;
            if (this.alpha < 0) {
                this.alpha = 0;
            }
        }
        @Override
        public void copyFrom(AlphaValue value){
            this.alpha=value.alpha;
        }
        @Override
        public void saveNBTData(CompoundNBT nbt){
            nbt.putInt("Alpha",alpha);
        }
    }

