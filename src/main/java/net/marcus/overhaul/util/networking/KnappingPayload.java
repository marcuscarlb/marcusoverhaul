package net.marcus.overhaul.util.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record KnappingPayload(int row, int col) implements CustomPayload {
    public static final CustomPayload.Id<KnappingPayload> ID = new CustomPayload.Id<>(KnappingPacket.GRID_CLICK_PACKET);
    public static final PacketCodec<RegistryByteBuf, KnappingPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, KnappingPayload::row,
            PacketCodecs.INTEGER, KnappingPayload::col,
            KnappingPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
