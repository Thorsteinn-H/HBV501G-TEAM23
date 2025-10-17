package is.hi.hbv501gteam23.Persistence.dto;

import is.hi.hbv501gteam23.Persistence.Entities.Player;

import java.time.LocalDate;

public final class PlayerDto {
    public record CreatePlayerRequest(
            String name,
            LocalDate dateOfBirth,
            String country,
            Player.PlayerPosition position,
            Integer goals,
            Long teamId
    ) {}

    public record PlayerResponse(
            Long id,
            String name,
            Player.PlayerPosition position,
            Integer goals,
            String country,
            LocalDate dateOfBirth,
            Long teamId,
            String teamName
    ) {}
}
