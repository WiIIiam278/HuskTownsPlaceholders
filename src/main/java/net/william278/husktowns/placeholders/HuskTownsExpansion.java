package net.william278.husktowns.placeholders;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.william278.husktowns.api.HuskTownsAPI;
import net.william278.husktowns.claim.Claim;
import net.william278.husktowns.claim.TownClaim;
import net.william278.husktowns.listener.Operation;
import net.william278.husktowns.town.Member;
import net.william278.husktowns.town.Role;
import net.william278.husktowns.town.Town;
import net.william278.husktowns.user.OnlineUser;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;
import java.util.UUID;

/**
 * PlaceholderAPI expansion for HuskTowns v2.x
 */
@SuppressWarnings("unused")
public class HuskTownsExpansion extends PlaceholderExpansion {

    private HuskTownsAPI api;

    @NotNull
    @Override
    public String getIdentifier() {
        return "husktowns";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "William278";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "2.0";
    }

    @NotNull
    @Override
    public String getRequiredPlugin() {
        return "HuskTowns";
    }

    @NotNull
    private String getBooleanValue(boolean bool) {
        return bool ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    @Override
    public boolean register() {
        if (super.register()) {
            api = HuskTownsAPI.getInstance();
            return true;
        }
        return false;
    }

    @Override
    public String onRequest(@Nullable OfflinePlayer offlinePlayer, @NotNull String params) {
        if (api == null || !api.isLoaded()) {
            return api == null ? "N/A" : api.getRawLocale("not_applicable").orElse("N/A");
        }

        // Ensure the player is online
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return api.getRawLocale("placeholder_player_offline")
                    .orElse("Player offline");
        }

        // Return the requested placeholder
        final OnlineUser player = api.getOnlineUser(offlinePlayer.getPlayer());
        return switch (params) {
            case "town_name" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getName)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_role" -> api.getUserTown(player)
                    .map(Member::role)
                    .map(Role::getName)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_mayor" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getMayor)
                    .map(uuid -> api.getUsername(uuid).join().orElse("?"))
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_color" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getColorRgb)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_members" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getMembers)
                    .map(members -> {
                        StringJoiner joiner = new StringJoiner(", ");
                        for (UUID member : members.keySet()) {
                            joiner.add(api.getUsername(member).join().orElse("?"));
                        }
                        return joiner.toString();
                    })
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_member_count" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getMembers)
                    .map(members -> String.valueOf(members.size()))
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_claim_count" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getClaimCount)
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_max_claims" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(town -> town.getMaxClaims(api.getPlugin()))
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_max_members" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(town -> town.getMaxMembers(api.getPlugin()))
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_money" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getMoney)
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_level_up_cost" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(town -> api.getPlugin().getLevels().getLevelUpCost(town.getLevel()))
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_level" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(Town::getLevel)
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "town_max_level" -> api.getUserTown(player)
                    .map(Member::town)
                    .map(town -> api.getPlugin().getLevels().getMaxLevel())
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_in_town")
                            .orElse("Not in town"));

            case "current_location_town" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::town)
                    .map(Town::getName)
                    .orElse(api.getRawLocale("placeholder_wilderness")
                            .orElse("Wilderness"));

            case "current_location_can_build" -> getBooleanValue(api.isOperationAllowed(
                    Operation.of(player, Operation.Type.BLOCK_PLACE, player.getPosition())));

            case "current_location_can_interact" -> getBooleanValue(api.isOperationAllowed(
                    Operation.of(player, Operation.Type.BLOCK_INTERACT, player.getPosition())));

            case "current_location_can_open_containers" -> getBooleanValue(api.isOperationAllowed(
                    Operation.of(player, Operation.Type.CONTAINER_OPEN, player.getPosition())));

            case "current_location_claim_type" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::claim)
                    .map(Claim::getType)
                    .map(Claim.Type::name)
                    .map(String::toLowerCase)
                    .orElse(api.getRawLocale("placeholder_wilderness")
                            .orElse("Wilderness"));

            case "current_location_plot_members" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::claim)
                    .map(claim -> {
                        if (claim.getType() == Claim.Type.PLOT) {
                            return api.getRawLocale("placeholder_not_a_plot")
                                    .orElse("Not a plot");
                        }

                        final StringJoiner joiner = new StringJoiner(", ");
                        for (UUID member : claim.getPlotMembers()) {
                            joiner.add(api.getUsername(member).join().orElse("?"));
                        }
                        return joiner.toString();
                    })
                    .orElse(api.getRawLocale("placeholder_not_claimed")
                            .orElse("Not claimed"));

            case "current_location_town_money" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::town)
                    .map(Town::getMoney)
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_claimed")
                            .orElse("Not claimed"));

            case "current_location_town_level" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::town)
                    .map(Town::getLevel)
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_claimed")
                            .orElse("Not claimed"));

            case "current_location_town_max_claims" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::town)
                    .map(town -> town.getMaxClaims(api.getPlugin()))
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_claimed")
                            .orElse("Not claimed"));

            case "current_location_town_max_members" -> api.getClaimAt(player.getPosition())
                    .map(TownClaim::town)
                    .map(town -> town.getMaxMembers(api.getPlugin()))
                    .map(String::valueOf)
                    .orElse(api.getRawLocale("placeholder_not_claimed")
                            .orElse("Not claimed"));

            default -> null;
        };
    }

}
