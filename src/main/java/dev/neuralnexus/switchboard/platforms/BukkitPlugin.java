/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/p0t4t0sandwich/Switchboard/blob/dev/LICENSE">GPL-3</a>
 * The API is Licensed under <a href="https://github.com/p0t4t0sandwich/Switchboard/blob/dev/LICENSE-API">MIT</a>
 */

package dev.neuralnexus.switchboard.platforms;

import dev.neuralnexus.switchboard.Switchboard;
import dev.neuralnexus.switchboard.SwitchboardPlugin;
import dev.neuralnexus.taterlib.logger.LoggerAdapter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/** Bukkit entry point. */
public class BukkitPlugin extends JavaPlugin implements SwitchboardPlugin {
    public BukkitPlugin() {
        pluginStart(
                this,
                Bukkit.getServer(),
                Bukkit.getLogger(),
                new LoggerAdapter(Switchboard.Constants.PROJECT_NAME, Bukkit.getLogger()));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
