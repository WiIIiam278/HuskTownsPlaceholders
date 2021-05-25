package com.extendedclip.expansions.example;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.william278.husktowns.HuskTownsAPI;
import me.william278.husktowns.object.chunk.ChunkType;
import me.william278.husktowns.object.chunk.ClaimedChunk;
import me.william278.husktowns.object.town.Town;
import me.william278.husktowns.object.town.TownRole;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public class HuskTownsExpansion extends PlaceholderExpansion {

    private final String VERSION = getClass().getPackage().getImplementationVersion();

    @Override
    public String getIdentifier() {
        return "husktowns";
    }

    @Override
    public String getAuthor() {
        return "William278";
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String getRequiredPlugin() {
        return "HuskTowns";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
    }

    @Override
    public boolean register() {
        Plugin huskTowns = Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if (huskTowns != null) {
            return super.register();
        }
        return false;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return "Player not online";
        }

        Player player = offlinePlayer.getPlayer();
        HuskTownsAPI huskTownsAPI = HuskTownsAPI.getInstance();
        if (huskTownsAPI == null) {
            return "HuskTowns is not enabled";
        }
        String town;
        switch (params) {
            case "town":
            case "town_name":
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                return town;
            case "town_role":
                TownRole role = huskTownsAPI.getPlayerTownRole(player);
                if (role == null) {
                    return "Not in town";
                }
                return role.toString().replace("_", " ").toLowerCase(Locale.ROOT);
            case "town_mayor":
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                return huskTownsAPI.getTownMayor(town);
            case "town_colour":
            case "town_color":
                if (huskTownsAPI.isInTown(player)) {
                    return Town.getTownColorHex(huskTownsAPI.getPlayerTown(player));
                } else {
                    return "#fffff";
                }
            case "town_members":
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                StringBuilder builder = new StringBuilder();
                for (String user : huskTownsAPI.getPlayersInTown(town)) {
                    builder.append(user).append(", ");
                }
                return builder.substring(0, builder.toString().length() - 2);
            case "town_member_count":
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                return Integer.toString(huskTownsAPI.getPlayersInTown(town).size());
            case "current_location_town":
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    return huskTownsAPI.getTownAt(player.getLocation());
                } else {
                    return "Wilderness";
                }
            case "current_location_can_build":
                if (huskTownsAPI.canBuild(player, player.getLocation())) {
                    return "Yes";
                } else {
                    return "No";
                }
            case "current_location_can_build_mark":
                if (huskTownsAPI.canBuild(player, player.getLocation())) {
                    return "✔";
                } else {
                    return "✘";
                }
            case "current_location_claim_type":
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    return huskTownsAPI.getClaimedChunk(player.getLocation()).getChunkType().toString().toLowerCase(Locale.ROOT);
                } else {
                    return "Wilderness";
                }
            case "current_location_claim_time":
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    return chunk.getFormattedTime();
                } else {
                    return "Not claimed";
                }
            case "current_location_claimer_uuid":
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    return chunk.getClaimerUUID().toString();
                } else {
                    return "Not claimed";
                }
            case "current_location_plot_owner_uuid":
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ChunkType.PLOT) {
                        return chunk.getPlotChunkOwner().toString();
                    } else {
                        return "Not a plot chunk";
                    }
                } else {
                    return "Not claimed";
                }
            default:
                return null;
        }
    }

}
