package dev.neuralnexus.tatercomms.forge.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.neuralnexus.tatercomms.common.commands.TaterCommsCommand;
import dev.neuralnexus.tatercomms.forge.ForgeTaterCommsPlugin;
import dev.neuralnexus.taterlib.common.hooks.LuckPermsHook;
import dev.neuralnexus.taterlib.forge.abstrations.player.ForgePlayer;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Forge implementation of the TaterComms command.
 */
@Mod.EventBusSubscriber(modid = ForgeTaterCommsPlugin.MOD_ID)
public final class ForgeTaterCommsCommand {
    /**
     * Registers the command.
     * @param event The register commands event.
     */
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        int permissionLevel;
        if (event.getEnvironment() == Commands.CommandSelection.DEDICATED) {
            // Check if LuckPerms is hooked
            permissionLevel = LuckPermsHook.isHooked() ? 0 : 4;
        } else {
            permissionLevel = 0;
        }

        // Register command
        event.getDispatcher().register(literal(TaterCommsCommand.getCommandName())
                .requires(source -> source.hasPermission(permissionLevel))
                .then(argument("command", StringArgumentType.greedyString())
                        .executes(context -> {
                            try {
                                String[] args = context.getArgument("command", String.class).split(" ");

                                // Check if sender is a player
                                boolean isPlayer = context.getSource().getEntity() instanceof Player;
                                ForgePlayer player = isPlayer ? new ForgePlayer((Player) context.getSource().getEntity()) : null;

                                // Execute command
                                TaterCommsCommand.executeCommand(player, isPlayer, args);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return 0;
                            }
                            return 1;
                        })
                ));
    }
}
