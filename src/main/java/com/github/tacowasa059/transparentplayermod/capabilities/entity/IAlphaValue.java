package com.github.tacowasa059.transparentplayermod.capabilities.entity;

import net.minecraft.nbt.CompoundNBT;

public interface IAlphaValue {
    void setAlpha(int alpha);
    int getAlpha();
    void increaseAlpha(int alpha);
    void decreaseAlpha(int alpha);
    void copyFrom(AlphaValue value);
    void saveNBTData(CompoundNBT nbt);
}
