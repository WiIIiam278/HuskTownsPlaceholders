package me.william278.husktowns.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.william278.husktowns.HuskTownsAPI;
import me.william278.husktowns.chunk.ClaimedChunk;
import me.william278.husktowns.town.TownRole;
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
        return "1.2.2";
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
        HuskTownsAPI huskTownsAPI = HuskTownsAPI.getInstance();
        if (huskTownsAPI == null) {
            return "HuskTowns is not enabled";
        }
        if (offlinePlayer == null) {
            return huskTownsAPI.getMessageString("placeholder_invalid_player");
        }

        Player player = offlinePlayer.getPlayer();
        if (player == null) {
            return huskTownsAPI.getMessageString("placeholder_player_offline");
        }
        String town;
        switch (params) {
            case "town":
            case "town_name":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                town = huskTownsAPI.getPlayerTown(player);
                return Objects.requireNonNullElse(town, huskTownsAPI.getMessageString("placeholder_not_in_town"));
            case "town_role":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                TownRole role = huskTownsAPI.getPlayerTownRole(player);
                if (role == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return role.toString().replace("_", " ").toLowerCase(Locale.ROOT);
            case "town_mayor":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return huskTownsAPI.getTownMayor(town);
            case "town_colour":
            case "town_color":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading_color");
                }
                if (huskTownsAPI.isInTown(player)) {
                    return huskTownsAPI.getTownColorHex(huskTownsAPI.getPlayerTown(player));
                } else {
                    return "#ffffff";
                }
            case "town_members":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                StringJoiner memberList = new StringJoiner(", ");
                for (String user : huskTownsAPI.getPlayersInTown(town)) {
                    memberList.add(user);
                }
                return memberList.toString();
            case "town_member_count":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return Integer.toString(huskTownsAPI.getPlayersInTown(town).size());
            case "town_coffer_balance":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return Double.toString(huskTownsAPI.getTownBalance(town));
            case "town_level":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                town = huskTownsAPI.getPlayerTown(player);
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return Integer.toString(huskTownsAPI.getTownLevel(town));
            case "current_location_town":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");
                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    return huskTownsAPI.getTownAt(player.getLocation());
                } else {
                    return "Wilderness";
                }
            case "current_location_town_color":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isStandingInTown(player)) {
                    return huskTownsAPI.getTownColorHex(huskTownsAPI.getTownAt(player.getLocation()));
                } else {
                    return huskTownsAPI.getMessageString("placeholder_wilderness_color");
                }
            case "current_location_can_build":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.canBuild(player, player.getLocation())) {
                    return huskTownsAPI.getMessageString("placeholder_yes");
                } else {
                    return huskTownsAPI.getMessageString("placeholder_no");
                }
            case "current_location_can_build_mark":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading_icon");
                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading_icon");
                }
                if (huskTownsAPI.canBuild(player, player.getLocation())) {
                    return huskTownsAPI.getMessageString("placeholder_tick");
                } else {
                    return huskTownsAPI.getMessageString("placeholder_cross");
                }
            case "current_location_claim_type":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    return huskTownsAPI.getClaimedChunk(player.getLocation()).getChunkType().toString().toLowerCase(Locale.ROOT);
                } else {
                    return huskTownsAPI.getMessageString("placeholder_wilderness");
                }
            case "current_location_claim_time":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    return chunk.getFormattedClaimTime();
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_claimer":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    return huskTownsAPI.getPlayerUsername(chunk.getClaimerUUID());
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_claimer_uuid":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    UUID chunkClaimerUUID = chunk.getClaimerUUID();
                    if (chunkClaimerUUID == null) {
                        return huskTownsAPI.getMessageString("placeholder_not_applicable");
                    }
                    return chunkClaimerUUID.toString();
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_town_mayor":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return huskTownsAPI.getTownMayor(town);
            case "current_location_plot_owner":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        if (chunk.getPlotChunkOwner() == null) {
                            return huskTownsAPI.getMessageString("placeholder_vacant");
                        }
                        return huskTownsAPI.getPlayerUsername(chunk.getPlotChunkOwner());
                    } else {
                        return huskTownsAPI.getMessageString("placeholder_not_a_plot_chunk");
                    }
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_plot_owner_uuid":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        if (chunk.getPlotChunkOwner() == null) {
                            return huskTownsAPI.getMessageString("placeholder_vacant");
                        }
                        return chunk.getPlotChunkOwner().toString();
                    } else {
                        return huskTownsAPI.getMessageString("placeholder_not_a_plot_chunk");
                    }
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_plot_members":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        if (chunk.getPlotChunkOwner() == null) {
                            return huskTownsAPI.getMessageString("placeholder_vacant");
                        }
                        StringJoiner currentLocationMemberList = new StringJoiner(", ");
                        for (UUID user : chunk.getPlotChunkMembers()) {
                            currentLocationMemberList.add(huskTownsAPI.getPlayerUsername(user));
                        }
                        return currentLocationMemberList.toString();
                    } else {
                        return huskTownsAPI.getMessageString("placeholder_not_a_plot_chunk");
                    }
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_plot_member_count":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (huskTownsAPI.isClaimed(player.getLocation())) {
                    ClaimedChunk chunk = huskTownsAPI.getClaimedChunk(player.getLocation());
                    if (chunk.getChunkType() == ClaimedChunk.ChunkType.PLOT) {
                        return Integer.toString(chunk.getPlotChunkMembers().size());
                    } else {
                        return huskTownsAPI.getMessageString("placeholder_not_a_plot_chunk");
                    }
                } else {
                    return huskTownsAPI.getMessageString("placeholder_not_claimed");
                }
            case "current_location_town_members":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                StringJoiner currentLocationMemberList = new StringJoiner(", ");
                for (String user : huskTownsAPI.getPlayersInTown(town)) {
                    currentLocationMemberList.add(user);
                }
                return currentLocationMemberList.toString();
            case "current_location_town_member_count":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return Integer.toString(huskTownsAPI.getPlayersInTown(town).size());
            case "current_location_town_coffer_balance":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return Double.toString(huskTownsAPI.getTownBalance(town));
            case "current_location_town_level":
                if (!huskTownsAPI.isPlayerCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                if (!huskTownsAPI.isClaimCacheLoaded()) {
                    return huskTownsAPI.getMessageString("placeholder_loading");

                }
                town = huskTownsAPI.getTownAt(player.getLocation());
                if (town == null) {
                    return huskTownsAPI.getMessageString("placeholder_not_in_town");
                }
                return Integer.toString(huskTownsAPI.getTownLevel(town));
            default:
                return null;
        }
    }

}
