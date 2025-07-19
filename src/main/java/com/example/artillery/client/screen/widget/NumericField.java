package com.example.artillery.client.screen.widget;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class NumericField extends EditBox {
    public NumericField(int x, int y, int w, int h) {
        super(Minecraft.getInstance().font, x, y, w, h, Component.empty());
        setFilter(s -> s.isEmpty() || s.chars().allMatch(ch -> ch=='-' || Character.isDigit(ch)));
    }
    public int getIntValue(int fallback) {
        try { return getValue().isEmpty()? fallback : Integer.parseInt(getValue()); }
        catch (NumberFormatException ex) { return fallback; }
    }
    public void setIntValue(int v) { setValue(Integer.toString(v)); }
}
