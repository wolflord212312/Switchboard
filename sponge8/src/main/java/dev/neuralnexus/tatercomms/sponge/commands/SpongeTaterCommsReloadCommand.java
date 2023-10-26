package dev.neuralnexus.tatercomms.sponge.commands;

import dev.neuralnexus.tatercomms.common.commands.TaterCommsCommand;
import dev.neuralnexus.taterlib.sponge.abstractions.player.SpongePlayer;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

public class SpongeTaterCommsReloadCommand implements CommandExecutor {
    /**
     * Register the command
     * @param container The plugin container
     * @param event The event
     */
    public void onRegisterCommands(PluginContainer container, final RegisterCommandEvent<Command.Parameterized> event) {
        // Register commands
        event.register(container, buildCommand(), "reload");
    }

    /**
     * Build the command
     * @return The command
     */
    public Command.Parameterized buildCommand(){
        return Command
                .builder()
                .executor(new SpongeTaterCommsReloadCommand())
                .permission("tatercomms.admin.reload")
                .shortDescription(Component.text("Reloads the config."))
                .build();
    }

    /**
     * @inheritDoc
     */
    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        try {
            String[] args = new String[]{"reload"};

            // Check if sender is a player
            boolean isPlayer = context.cause().root() instanceof Player;
            SpongePlayer player = isPlayer ? new SpongePlayer((Player) context.cause().root()) : null;

            // Execute command
            TaterCommsCommand.executeCommand(player, isPlayer, args);
        } catch (Exception e) {
            e.printStackTrace();
            return CommandResult.builder()
                    .result(0).error(Component.text(e.getMessage())).build();
        }
        return CommandResult.builder()
                .result(1).build();
    }
}
