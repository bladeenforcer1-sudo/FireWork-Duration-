package themasterkitty.fireworkindicator;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;

public final class Main implements ClientModInitializer {
    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(
            "fireworkindicator", "textures/firework.png");
    private static final ResourceLocation GRAY_ICON = ResourceLocation.fromNamespaceAndPath(
            "fireworkindicator", "textures/fireworkgray.png");

    private static long lastUsedMillis = 0L;
    private static int durationMillis = 0;

    @Override
    public void onInitializeClient() {
        UseItemCallback.EVENT.register((player, level, hand) -> {
            if (level.isClientSide() && player == Minecraft.getInstance().player
                    && player.isFallFlying()) {
                ItemStack stack = player.getItemInHand(hand);
                if (stack.is(Items.FIREWORK_ROCKET)) {
                    Fireworks fireworks = stack.get(net.minecraft.core.component.DataComponents.FIREWORKS);
                    int flight = fireworks == null ? 1 : fireworks.flightDuration();
                    // Keep the original indicator's approximate timing for flight durations 1–3.
                    int[] durations = {1170, 1550, 2220};
                    durationMillis = durations[Math.max(0, Math.min(2, flight - 1))];
                    lastUsedMillis = System.currentTimeMillis();
                }
            }
            return InteractionResult.PASS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || !client.player.isFallFlying()) {
                lastUsedMillis = 0L;
            }
        });

        HudRenderCallback.EVENT.register((graphics, tickCounter) -> render(graphics));
    }

    private static void render(GuiGraphics graphics) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui || !client.player.isFallFlying()
                || lastUsedMillis == 0L || durationMillis <= 0) return;

        long elapsed = System.currentTimeMillis() - lastUsedMillis;
        if (elapsed >= durationMillis) return;

        double progress = Math.min(1.0, (double) elapsed / durationMillis);
        int x = graphics.guiWidth() / 2 - 6;
        int y = graphics.guiHeight() / 2 - 11;
        int remainingHeight = (int) Math.ceil(12.0 * (1.0 - progress));

        graphics.blit(ICON, x, y, 0, 0, 12, 12, 12, 12);
        if (remainingHeight > 0) {
            int gone = 12 - remainingHeight;
            graphics.blit(GRAY_ICON, x, y + gone, 0, gone, 12, remainingHeight, 12, 12);
        }
    }
}
