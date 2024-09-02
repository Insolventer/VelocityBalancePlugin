package net.pashmash.velocitybalanceplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.slf4j.Logger;

import java.util.ArrayList;

@Plugin(
        id = "velocitybalanceplugin",
        name = "VelocityBalancePlugin",
        version = "1.0.0",
        authors = {"Pashmash"}
)
public class VelocityBalancePlugin {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer server;

    private int nextServerIndex = 0;

    private final ArrayList<RegisteredServer> servers = new ArrayList<>();

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        server.getServer("hub-1").ifPresent(servers::add);
        server.getServer("hub-2").ifPresent(servers::add);

        if (servers.size() > 0) {
            RegisteredServer nextServer = servers.get(nextServerIndex);
            event.setInitialServer(nextServer);
            nextServerIndex = (nextServerIndex + 1) % servers.size();
        }
    }

    @Subscribe
    public void onPlayerKickedFromServer(KickedFromServerEvent event) {
        ArrayList<RegisteredServer> servers = new ArrayList<>();
        server.getServer("hub-1").ifPresent(servers::add);
        server.getServer("hub-2").ifPresent(servers::add);

        if (servers.size() > 0) {
            RegisteredServer nextServer = servers.get(nextServerIndex);
            event.setResult(KickedFromServerEvent.RedirectPlayer.create(nextServer));
            nextServerIndex = (nextServerIndex + 1) % servers.size();
        }
    }
}