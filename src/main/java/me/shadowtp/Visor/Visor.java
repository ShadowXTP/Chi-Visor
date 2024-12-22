package me.shadowtp.Visor;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Visor extends ChiAbility implements AddonAbility {

    private static Permission perm;
    private static VisorListener listener;

    private long cooldown = ConfigManager.getConfig().getInt("ExtraAbilities.ShadowTP.Visor.Cooldown");
    private long duration = ConfigManager.getConfig().getInt("ExtraAbilities.ShadowTP.Visor.Duration");

    public Visor(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) {
            return;
        }

        loadConfig();


        ChiAbility stance = bPlayer.getStance();
        if (stance != null) {
            stance.remove();
            if (stance instanceof Visor) {
                bPlayer.setStance(null);
                return;
            }
        }
        activate();
    }

    private void activate() {
        bPlayer.setStance(this);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.5F);
        start();
    }

    @Override
    public void progress() {
        if (!bPlayer.canBendIgnoreBinds(this) || !bPlayer.hasElement(Element.CHI)) {
            remove();
            return;
        }

        if (!player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
        }


        if (duration > 0 && System.currentTimeMillis() > getStartTime() + duration) {
            remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        bPlayer.addCooldown(this);
        bPlayer.setStance(null);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0F, 1.5F);
    }




    public void load() {

        listener = new VisorListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);


        perm = new Permission("bending.ability.Visor", PermissionDefault.OP);
        ProjectKorra.plugin.getServer().getPluginManager().addPermission(perm);


        ConfigManager.getConfig().addDefault("ExtraAbilities.ShadowTP.Visor.Cooldown", 5000);
        ConfigManager.getConfig().addDefault("ExtraAbilities.ShadowTP.Visor.Duration", 0); // 0 for infinite duration
        ConfigManager.defaultConfig.save();


        ProjectKorra.plugin.getLogger().info(getName() + " " + getVersion() + " by " + getAuthor() + " has been successfully enabled.");
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(listener);
        ProjectKorra.plugin.getServer().getPluginManager().removePermission(perm);
    }

    @Override
    public String getAuthor() {
        return "ShadowTP";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    private void loadConfig() {

    }


    @Override
    public String getName() {
        return "Visor";
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return true;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public String getDescription() {
        return "Toggle a stance that grants enhanced night vision for improved visibility in darkness. Credit: Heitai_Yukanna";
    }

    @Override
    public String getInstructions() {
        return "Left-click to toggle the Visor stance.";
    }

}
