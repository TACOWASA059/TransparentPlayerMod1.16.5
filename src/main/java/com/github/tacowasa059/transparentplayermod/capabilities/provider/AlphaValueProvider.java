package com.github.tacowasa059.transparentplayermod.capabilities.provider;

import com.github.tacowasa059.transparentplayermod.capabilities.entity.AlphaValue;
import com.github.tacowasa059.transparentplayermod.capabilities.entity.IAlphaValue;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//data get entity Dev ForgeCaps."transparentplayermod:alpha"
public class AlphaValueProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(AlphaValue.class)
    public static final Capability<AlphaValue> capability=null;
    private LazyOptional<AlphaValue> instance=LazyOptional.of(capability::getDefaultInstance);;

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == capability ? instance.cast() : LazyOptional.empty();
    }
    @Override
    public INBT serializeNBT() {
        return  capability.getStorage().writeNBT(capability, instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        capability.getStorage().readNBT(capability, instance.orElseThrow(
                () -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
    }
}
