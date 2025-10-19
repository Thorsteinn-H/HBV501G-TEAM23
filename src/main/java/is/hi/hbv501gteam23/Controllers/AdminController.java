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
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final MatchService matchService;

    // ===================== TEAMS =====================

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teams")
    public Team createTeam(Team team){
        if(teamService.findByName(team.getName())!=null) {
            return null;
        }
        return teamService.create(team);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/teams/{id}")
    public void deleteTeam(@PathVariable Long id){
        teamService.deleteTeam(id);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/teams/{id}")
    public Team updateTeam(@PathVariable Team team){
        Long id = team.getId();
        if(id!=null) {
            return null;
        }
        return teamService.update(team);
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
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/players")
    public ResponseEntity<PlayerDto.PlayerResponse> createPlayer(@RequestBody PlayerDto.CreatePlayerRequest body) {
        Player created = playerService.createPlayer(
                body.name(), body.dateOfBirth(), body.country(),
                body.position(), body.goals(), body.teamId()
        );
        URI location = URI.create("/players/" + created.getId());
        return ResponseEntity.created(location).body(toResponse(created));
    }

    /**
     * Updates an existing player.
     * @param player player entity carrying the updated state
     * @return the updated {@link Player} entity
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/players/{id}")
    public Player updatePlayer(@RequestBody Player player) {
        return playerService.updatePlayer(player);
    }

    /**
     * Deletes a player by id.
     * @param id identifier of the player to remove
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    // ===================== MATCHES =====================

    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/matches")
    public Match createMatch(Match match){
        return matchService.createMatch(match);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/matches/{id}")
    public Match updateMatch(@PathVariable Match match){
        return matchService.updateMatch(match);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/matches/{id}")
    public void deleteMatch(@PathVariable Long id){
        matchService.deleteMatch(id);
    }

    // ===================== MAPPERS =====================

    private PlayerDto.PlayerResponse toResponse(Player p) {
        return new PlayerDto.PlayerResponse(
                p.getId(),
                p.getName(),
                p.getPosition(),
                p.getGoals(),
                p.getCountry(),
                p.getDateOfBirth(),
                p.getTeam() != null ? p.getTeam().getId()   : null,
                p.getTeam() != null ? p.getTeam().getName() : null
        );
    }

    private TeamDto.TeamResponse toResponse(Team t) {
        return new TeamDto.TeamResponse(
                t.getId(),
                t.getName(),
                t.getCountry(),
                t.getVenue().getId(),
                t.getVenue().getName()
        );
    }

    private MatchDto.MatchResponse toResponse(Match m) {
        return new MatchDto.MatchResponse(
                m.getId(),
                m.getDate(),
                m.getHomeTeam() != null ? m.getHomeTeam().getId()   : null,
                m.getHomeTeam() != null ? m.getHomeTeam().getName() : null,
                m.getAwayTeam() != null ? m.getAwayTeam().getId()   : null,
                m.getAwayTeam() != null ? m.getAwayTeam().getName() : null,
                m.getVenue()    != null ? m.getVenue().getId()      : null,
                m.getVenue()    != null ? m.getVenue().getName()    : null,
                m.getHomeGoals(),
                m.getAwayGoals()
        );
    }
}
