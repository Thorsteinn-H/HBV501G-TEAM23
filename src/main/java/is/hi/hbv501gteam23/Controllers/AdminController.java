package is.hi.hbv501gteam23.Controllers;

import is.hi.hbv501gteam23.Persistence.Entities.Match;
import is.hi.hbv501gteam23.Persistence.Entities.Player;
import is.hi.hbv501gteam23.Persistence.Entities.Team;
import is.hi.hbv501gteam23.Persistence.dto.MatchDto;
import is.hi.hbv501gteam23.Persistence.dto.PlayerDto;
import is.hi.hbv501gteam23.Persistence.dto.TeamDto;
import is.hi.hbv501gteam23.Services.Interfaces.MatchService;
import is.hi.hbv501gteam23.Services.Interfaces.PlayerService;
import is.hi.hbv501gteam23.Services.Interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller that exposes read/write operations for {@link Team}, {@link Match} and {@link Player} resources.
 * Base path is /admin
 * Only endpoints are {push, patch, delete} for Admin role to perform.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final MatchService matchService;

    // ===================== TEAMS =====================

    /**
     * Creates a new team.
     *
     * <p>
     *     This method saves a new {@link Team} entity to the database.
     * </p>
     * @param team the {@link Team} object to be created
     * @return the created {@link Team} entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teams")
    public Team createTeam(Team team) {
        if (teamService.findByName(team.getName()) != null) {
            return null;
        }
        return teamService.create(team);
    }

    /**
     * Deletes a team.
     *
     * <p>
     *     This method deletes a {@link Team} entity in the database.
     * </p>
     * @param id the id of the team to be deleted.
     *
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Get the current authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Add the username to the model to display in the view
        model.addAttribute("username", authentication.getName());

        // Return the admin dashboard view
        return "admin/dashboard";
    }

    /**
     * Partially updates existing team
     *
     * @param id the id of the team to update
     * @param body partial update payload for the team
     * @return 200 OK with the updated {@link TeamDto.TeamResponse};
     *         404 if the match (or a referenced team/venue) is not found; *Not implemented yet*
     *         400 if the payload is invalid                               *Not implemented yet*
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/teams/{id}")
    public ResponseEntity<TeamDto.TeamResponse> patchTeam(
            @PathVariable Long id,
            @RequestBody TeamDto.PatchTeamRequest body
    ) {
        Team updated = teamService.patchTeam(id, body);
        return ResponseEntity.ok(toResponse(updated));
    }

    // ===================== PLAYERS =====================

    // Start/part of Use case 1 doesn't work yet.
    /**
     * Creates a new player.
     * @param body request payload containing player properties and target {@code teamId}
     * @return a 201 response with the created {@link PlayerDto.PlayerResponse} body
     * @throws jakarta.persistence.EntityNotFoundException if {@code teamId} does not exist
     * @apiNote Part of “Use case 1” (as noted in code comments) though not fully implemented in UX yet.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/players")
    public ResponseEntity<PlayerDto.PlayerResponse> createPlayer(@RequestBody PlayerDto.CreatePlayerRequest body){
        Player created = playerService.createPlayer(
                body.name(), body.dateOfBirth(), body.country(),
                body.position(), body.goals(), body.teamId()
        );
        URI location = URI.create("/players/" + created.getId());
        return ResponseEntity.created(location).body(toResponse(created));
    }

    /**
     * Partially updates existing player
     *
     * @param id the id of the player to update
     * @param body partial update payload for the player
     * @return 200 OK with the updated {@link PlayerDto.PlayerResponse};
     *         404 if the match (or a referenced team/venue) is not found; *Not implemented yet*
     *         400 if the payload is invalid                               *Not implemented yet*
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/players/{id}")
    public ResponseEntity<PlayerDto.PlayerResponse> patchPlayer(
            @PathVariable Long id,
            @RequestBody PlayerDto.PatchPlayerRequest body
    ) {
        Player updated = playerService.patchPlayer(id, body);
        return ResponseEntity.ok(toResponse(updated));
    }

    /**
     * Deletes a player by id.
     * @param id identifier of the player to remove
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id){
        playerService.deletePlayer(id);
    }

    // ===================== MATCHES =====================

    /**
     * Creates a match
     *
     * <p>
     *     This method saves a new {@link Match} entity to the database.
     * </p>
     * @param match the {@link Match} object to be created
     * @return the created {@link Match} entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/matches")
    public Match createMatch (Match match){
        return matchService.createMatch(match);
    }

    /**
     * Partially updates existing match
     *
     * @param id the id of the match to update
     * @param body partial update payload for the match
     * @return 200 OK with the updated {@link MatchDto.MatchResponse};
     *         404 if the match (or a referenced team/venue) is not found; *Not implemented yet*
     *         400 if the payload is invalid                               *Not implemented yet*
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/matches/{id}")
    public ResponseEntity<MatchDto.MatchResponse> patchMatch(
            @PathVariable Long id,
            @RequestBody MatchDto.PatchMatchRequest body
    ) {
        Match updated = matchService.patchMatch(id, body);
        return ResponseEntity.ok(toResponse(updated));
    }

    /**
     * Deletes a match by its id
     *
     * <p>
     *     This method deletes a {@link Match} entity in the database.
     * </p>
     *
     * @param id the id of the match to be deleted
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id){
        matchService.deleteMatch(id);
    }

    // ===================== MAPPERS =====================

    /**
     * Maps a {@link Player} entity to a {@link PlayerDto.PlayerResponse} DTO.
     * @param p player entity
     * @return mapped {@link PlayerDto.PlayerResponse}
     */
    private PlayerDto.PlayerResponse toResponse(Player p) {
        return new PlayerDto.PlayerResponse(
                p.getId(),
                p.getName(),
                p.isActive(),
                p.getPosition(),
                p.getGoals(),
                p.getCountry(),
                p.getDateOfBirth(),
                p.getTeam() != null ? p.getTeam().getId() : null,
                p.getTeam() != null ? p.getTeam().getName() : null
        );
    }

    /**
     * Maps a {@link Team} entity to a {@link TeamDto.TeamResponse} DTO.
     * @param t team entity
     * @return mapped {@link TeamDto.TeamResponse}
     */
    private TeamDto.TeamResponse toResponse(Team t) {
        return new TeamDto.TeamResponse(
                t.getId(),
                t.getName(),
                t.isActive(),
                t.getCountry(),
                t.getVenue() != null ? t.getVenue().getId() : null,
                t.getVenue() != null ? t.getVenue().getName() : null
        );
    }

    /**
     * Maps a {@link Match} entity to a {@link MatchDto.MatchResponse} DTO.
     * @param m match entity
     * @return mapped {@link MatchDto.MatchResponse}
     */
    private MatchDto.MatchResponse toResponse(Match m) {
        return new MatchDto.MatchResponse(
                m.getId(),
                m.getDate(),
                m.getHomeTeam() != null ? m.getHomeTeam().getId() : null,
                m.getHomeTeam() != null ? m.getHomeTeam().getName() : null,
                m.getAwayTeam() != null ? m.getAwayTeam().getId() : null,
                m.getAwayTeam() != null ? m.getAwayTeam().getName() : null,
                m.getVenue() != null ? m.getVenue().getId() : null,
                m.getVenue() != null ? m.getVenue().getName() : null,
                m.getHomeGoals(),
                m.getAwayGoals()
        );
    }
}
