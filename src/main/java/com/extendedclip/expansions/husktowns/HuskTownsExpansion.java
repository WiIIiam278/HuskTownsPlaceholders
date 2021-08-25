package com.extendedclip.expansions.husktowns;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.william278.husktowns.HuskTowns;
import me.william278.husktowns.HuskTownsAPI;
import me.william278.husktowns.object.cache.Cache;
import me.william278.husktowns.object.chunk.ClaimedChunk;
import me.william278.husktowns.object.town.Town;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class HuskTownsExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "husktowns";
    }

    @Override
    public @NotNull String getAuthor() {
        return "William278";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1.2";
    }

    @Override
    public @NotNull String getRequiredPlugin() {
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
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return "Invalid player";
        }

        Player player = offlinePlayer.getPlayer();
        if (player == null) {
            return "Player not online";
        }
        HuskTownsAPI huskTownsAPI = HuskTownsAPI.getInstance();
        if (huskTownsAPI == null) {
            return "HuskTowns is not enabled";
        }
        String town;
        switch (params) {
            case "town":
            case "town_name":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getPlayerTown(player);
                return Objects.requireNonNullElse(town, "Not in town");
            case "town_role":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                Town.TownRole role = huskTownsAPI.getPlayerTownRole(player);
                if (role == null) {
                    return "Not in town";
                }
                return role.toString().replace("_", " ").toLowerCase(Locale.ROOT);
            case "town_mayor":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                return huskTownsAPI.getTownMayor(town);
            case "town_colour":
            case "town_color":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "#aaaaaa";
                }
                if (huskTownsAPI.isInTown(player)) {
                    return Town.getTownColorHex(huskTownsAPI.getPlayerTown(player));
                } else {
                    return "#ffffff";
                }
            case "town_members":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                StringJoiner memberList = new StringJoiner(", ");
                for (String user : huskTownsAPI.getPlayersInTown(town)) {
                    memberList.add(user);
                }
                return memberList.toString();
            case "town_member_count":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return "Not in town";
                }
                return Integer.toString(huskTownsAPI.getPlayersInTown(town).size());
            case "current_location_town":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    return huskTownsAPI.getTownAt(player.getLocation());
                } else {
                    return "Wilderness";
                }
            case "current_location_town_color":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isStandingInTown(player)) {
                    return Town.getTownColorHex(huskTownsAPI.getTownAt(player.getLocation()));
                } else {
                    return "#2e2e2e";
                }
            case "current_location_can_build":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.canBuild(player, player.getLocation())) {
                    return "Yes";
                } else {
                    return "No";
                }
            case "current_location_can_build_mark":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "⌚";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "⌚";
                }
                if (huskTownsAPI.canBuild(player, player.getLocation())) {
                    return "✔";
                } else {
                    return "✘";
                }
            case "current_location_claim_type":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    return huskTownsAPI.getClaimedChunk(player.getLocation()).getChunkType().toString().toLowerCase(Locale.ROOT);
                } else {
                    return "Wilderness";
                }
            case "current_location_claim_time":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    return chunk.getFormattedClaimTime();
                } else {
                    return "Not claimed";
                }
            case "current_location_claimer":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    return HuskTowns.getPlayerCache().getPlayerUsername(chunk.getClaimerUUID());
                } else {
                    return "Not claimed";
                }
            case "current_location_claimer_uuid":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    UUID chunkClaimerUUID = chunk.getClaimerUUID();
                    if (chunkClaimerUUID == null) {
                        return "N/A";
                    }
                    return chunkClaimerUUID.toString();
                } else {
                    return "Not claimed";
                }
            case "current_location_town_mayor":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return "Not in town";
                }
                return huskTownsAPI.getTownMayor(town);
            case "current_location_plot_owner":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        return HuskTowns.getPlayerCache().getPlayerUsername(chunk.getPlotChunkOwner());
                    } else {
                        return "Not a plot chunk";
                    }
                } else {
                    return "Not claimed";
                }
            case "current_location_plot_owner_uuid":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        return chunk.getPlotChunkOwner().toString();
                    } else {
                        return "Not a plot chunk";
                    }
                } else {
                    return "Not claimed";
                }
            case "current_location_plot_members":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        StringJoiner currentLocationMemberList = new StringJoiner(", ");
                        for (UUID user : chunk.getPlotChunkMembers()) {
                            currentLocationMemberList.add(HuskTowns.getPlayerCache().getPlayerUsername(user));
                        }
                        return currentLocationMemberList.toString();
                    } else {
                        return "Not a plot chunk";
                    }
                } else {
                    return "Not claimed";
                }
            case "current_location_plot_member_count":
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        return Integer.toString(chunk.getPlotChunkMembers().size());
                    } else {
                        return "Not a plot chunk";
                    }
                } else {
                    return "Not claimed";
                }
            case "current_location_town_members":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return "Not in town";
                }
                StringJoiner currentLocationMemberList = new StringJoiner(", ");
                for (String user : huskTownsAPI.getPlayersInTown(town)) {
                    currentLocationMemberList.add(user);
                }
                return currentLocationMemberList.toString();
            case "current_location_town_member_count":
                if (huskTownsAPI.getPlayerCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                if (huskTownsAPI.getClaimedCacheStatus() != Cache.CacheStatus.LOADED) {
                    return "(Loading...)";
                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return "Not in town";
                }
                return Integer.toString(huskTownsAPI.getPlayersInTown(town).size());
            default:
                return null;
        }
    }

}
