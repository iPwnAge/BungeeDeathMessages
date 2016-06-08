package com.ipwnage.bungeedeathmessages;


import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeDeathMessages extends JavaPlugin implements PluginMessageListener, Listener {
    public static final Logger log = Logger.getLogger("Minecraft");
    private static BungeeDeathMessages instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getPluginManager().registerEvents(this, this);

    }
    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("Deathmessages");
        String stream = e.getDeathMessage();
        byte[] data = stream.getBytes();
        out.writeShort(data.length);
        out.write(data);
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());


    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message){
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("Deathmessages")) {
            String subChannel = in.readUTF();
            getServer().broadcastMessage( subChannel );
        }

    }

    private static BungeeDeathMessages getInstance() {
        return instance;
    }

}
