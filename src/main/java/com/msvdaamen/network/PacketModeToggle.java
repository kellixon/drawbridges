package com.msvdaamen.network;

import com.msvdaamen.tileentities.TileEntityAdvDrawbridge;
import com.msvdaamen.tileentities.TileEntityPulseDrawbridge;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketModeToggle implements IMessage {
    private boolean mode;
    private BlockPos blockPos;

    public PacketModeToggle() {

    }

    public PacketModeToggle(boolean mode, BlockPos pos) {
        this.mode = mode;
        blockPos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mode = buf.readBoolean();
        blockPos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(mode);
        buf.writeLong(blockPos.toLong());
    }

    public static class Handler implements IMessageHandler<PacketModeToggle, IMessage> {

        public Handler() {}

        @Override
        public IMessage onMessage(PacketModeToggle message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketModeToggle message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.getServerWorld();
            if(world.getTileEntity(message.blockPos) instanceof TileEntityPulseDrawbridge) {
                TileEntityPulseDrawbridge tile = (TileEntityPulseDrawbridge) world.getTileEntity(message.blockPos);
                tile.setMode(message.mode);
            }
            if(world.getTileEntity(message.blockPos) instanceof TileEntityAdvDrawbridge) {
                TileEntityAdvDrawbridge tile = (TileEntityAdvDrawbridge) world.getTileEntity(message.blockPos);
                tile.setMode(message.mode);
            }
        }
    }
}
