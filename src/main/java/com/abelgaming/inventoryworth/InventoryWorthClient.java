package com.abelgaming.inventoryworth;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import java.text.NumberFormat;

public class InventoryWorthClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            int emeraldBlocks = 0;
            int diamondBlocks = 0;
            int goldBlocks = 0;

            for (ItemStack stack : client.player.getInventory().main) {
                if (stack.getItem() == Items.EMERALD_BLOCK) {
                    emeraldBlocks += stack.getCount();
                } else if (stack.getItem() == Items.DIAMOND_BLOCK) {
                    diamondBlocks += stack.getCount();
                } else if (stack.getItem() == Items.GOLD_BLOCK) {
                    goldBlocks += stack.getCount();
                }
            }

            int emeraldValue = emeraldBlocks * 864;
            int diamondValue = diamondBlocks * 432;
            int goldValue = goldBlocks * 216;

            NumberFormat nf = NumberFormat.getIntegerInstance();

            int xStart = 10;
            int yStart = 10;
            int iconSize = 16;
            int padding = 4;

            // Emerald
            ItemStack emeraldStack = new ItemStack(Items.EMERALD_BLOCK);
            client.getItemRenderer().renderGuiItemIcon(matrixStack, emeraldStack, xStart, yStart);
            client.textRenderer.drawWithShadow(matrixStack, "Emerald Value: $" + nf.format(emeraldValue), xStart + iconSize + padding, yStart + 4, 0x00FF00);

            // Diamond
            ItemStack diamondStack = new ItemStack(Items.DIAMOND_BLOCK);
            client.getItemRenderer().renderGuiItemIcon(matrixStack, diamondStack, xStart, yStart + 20);
            client.textRenderer.drawWithShadow(matrixStack, "Diamond Value: $" + nf.format(diamondValue), xStart + iconSize + padding, yStart + 20 + 4, 0x00FFFF);

            // Gold
            ItemStack goldStack = new ItemStack(Items.GOLD_BLOCK);
            client.getItemRenderer().renderGuiItemIcon(matrixStack, goldStack, xStart, yStart + 40);
            client.textRenderer.drawWithShadow(matrixStack, "Gold Value: $" + nf.format(goldValue), xStart + iconSize + padding, yStart + 40 + 4, 0xFFD700);
        });
    }
}
