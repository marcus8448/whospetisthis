package io.github.marcus8448.mods.whospetisthis;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public class WhosPetIsThis implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) -> {
            commandDispatcher.register(CommandManager.literal("getowner").then(CommandManager.argument("entity", EntityArgumentType.entity()).executes(context -> {
                Entity entity = EntityArgumentType.getEntity(context, "entity");
                if (entity instanceof TameableEntity) {
                    UUID uuid = ((TameableEntity) entity).getOwnerUuid();
                    GameProfile profile = context.getSource().getMinecraftServer().getUserCache().getByUuid(uuid);
                    if (profile != null) {
                        context.getSource().sendFeedback(new LiteralText("Pet owner: " + profile.getName()), false);
                    } else {
                        context.getSource().sendFeedback(new LiteralText("Pet owner: " + (uuid == null ? "null" : uuid.toString())), false);
                    }
                    return 1;
                } else {
                    context.getSource().sendError(new LiteralText("Selected entity is not tamable!"));
                }
                return 0;
            })));
        });
    }
}

