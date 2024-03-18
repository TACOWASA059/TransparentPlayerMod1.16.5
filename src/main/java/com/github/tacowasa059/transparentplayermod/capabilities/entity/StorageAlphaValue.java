package com.github.tacowasa059.transparentplayermod.capabilities.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class StorageAlphaValue implements Capability.IStorage<AlphaValue> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<AlphaValue> capability, AlphaValue instance, Direction side) {
            // データをNBTに保存
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("alpha", instance.getAlpha());
            return tag;
        }
    @Override
    public void readNBT(Capability<AlphaValue> capability, AlphaValue instance, Direction direction, INBT inbt) {
        CompoundNBT tag = (CompoundNBT) inbt;
        instance.setAlpha(tag.getInt("alpha"));
    }
}
