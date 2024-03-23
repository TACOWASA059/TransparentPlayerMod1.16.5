package com.github.tacowasa059.transparentplayermod.capabilities.entity;

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
        public void copyFrom(AlphaValue value){
            this.alpha=value.alpha;
        }
}

