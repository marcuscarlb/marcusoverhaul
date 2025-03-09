package net.marcus.overhaul.util.networking;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.marcus.overhaul.MarcusMinecraftOverhaul;
import net.marcus.overhaul.util.screen.KnappingScreenHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class KnappingPacket {
    public static final Identifier GRID_CLICK_PACKET = Identifier.of(MarcusMinecraftOverhaul.MOD_ID, "grid_click");

    public static void register() {
        PayloadTypeRegistry.playC2S().register(KnappingPayload.ID, KnappingPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(KnappingPayload.ID, ((payload, context) -> {
            int row = payload.row();
            int col = payload.col();

            Objects.requireNonNull(context.player().getServer()).execute(() -> {
                if (context.player().currentScreenHandler instanceof KnappingScreenHandler) {
                    KnappingScreenHandler handler = ((KnappingScreenHandler) context.player().currentScreenHandler);
                    handler.handleGridClick(row,col, context.player());
                }
            });
        }));
    }
}
