package me.shadowtp.Visor;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.event.PlayerSwingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public class VisorListener implements Listener {

    @EventHandler
    public void onPlayerSwing(PlayerSwingEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        String boundAbilityName = bPlayer.getBoundAbilityName();


        // Activate the Visor ability
        if (CoreAbility.hasAbility(player, Visor.class) && boundAbilityName.equalsIgnoreCase("Visor")) {
            // If already active, deactivate it
            CoreAbility.getAbility(player, Visor.class).remove();
        } else {
            // Activate the Visor stance
            new Visor(player);
        }
    }
}
