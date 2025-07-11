package com.abelgaming.inventoryworth;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.text.NumberFormat;

public class InventoryWorthClient implements ClientModInitializer {

    private static int hudX = 30;
    private static int hudY = 10;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((MatrixStack matrixStack, float tickDelta) -> {
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

            int totalValue = emeraldValue + diamondValue + goldValue;

            NumberFormat nf = NumberFormat.getIntegerInstance();

            int iconSize = 16;
            int padding = 4;

            client.getItemRenderer().renderGuiItemIcon(matrixStack, new ItemStack(Items.EMERALD_BLOCK), hudX, hudY);
            client.textRenderer.drawWithShadow(matrixStack, "Emerald Value: $" + nf.format(emeraldValue), hudX + iconSize + padding, hudY + 4, 0x00FF00);

            client.getItemRenderer().renderGuiItemIcon(matrixStack, new ItemStack(Items.DIAMOND_BLOCK), hudX, hudY + 20);
            client.textRenderer.drawWithShadow(matrixStack, "Diamond Value: $" + nf.format(diamondValue), hudX + iconSize + padding, hudY + 24, 0x00FFFF);

            client.getItemRenderer().renderGuiItemIcon(matrixStack, new ItemStack(Items.GOLD_BLOCK), hudX, hudY + 40);
            client.textRenderer.drawWithShadow(matrixStack, "Gold Value: $" + nf.format(goldValue), hudX + iconSize + padding, hudY + 44, 0xFFD700);

            client.getItemRenderer().renderGuiItemIcon(matrixStack, new ItemStack(Items.MAGMA_CREAM), hudX, hudY + 60);
            client.textRenderer.drawWithShadow(matrixStack, "Inventory Value: $" + nf.format(totalValue), hudX + iconSize + padding, hudY + 64, 0xFFD700);
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommandManager.literal("setworthpos")
                            .then(ClientCommandManager.argument("x", IntegerArgumentType.integer(0, 3000))
                                    .then(ClientCommandManager.argument("y", IntegerArgumentType.integer(0, 3000))
                                            .executes(context -> setPositionCommand(context))))
            );
        });
    }

    private int setPositionCommand(CommandContext<?> context) {
        hudX = IntegerArgumentType.getInteger(context, "x");
        hudY = IntegerArgumentType.getInteger(context, "y");
        MinecraftClient.getInstance().player.sendMessage(Text.literal("Inventory worth HUD moved to: " + hudX + ", " + hudY), false);
        return 1;
    }
}
