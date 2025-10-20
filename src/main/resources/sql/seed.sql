INSERT INTO venue (venue_id, venue_name, latitude, longitude)
VALUES
    (1, 'Laugardalsvöllur', 0.5345, 0.2342),
    (2, 'Kópavogsvöllur', 0.98524, 0.32452),
    (3, 'Þórsvöllur', 0.234452, 0.234562);

INSERT INTO team (team_id, team_name, is_active, team_country, venue_id)
    VALUES
        (1, 'Þróttur', true, 'Iceland', 1),
        (2, 'Breiðablik', true, 'Iceland', 2),
        (3, 'Þór', true, 'Iceland', 3);

INSERT INTO player (player_id, player_name, is_active, date_of_birth, player_country, player_position, goals, team_id)
    VALUES
        (1, 'John Snow', true,'2007-12-03', 'England', 'GOALKEEPER', 13, 1),
        (2, 'Ariana Grande', true, '1995-08-30', 'United states of America', 'DEFENDER', 2, 2),
        (3, 'Jón Pétur Karlsson', true, '2000-08-13', 'Iceland', 'MIDFIELDER', 9, 3);

INSERT INTO matches (match_id, match_date, home_goals, away_goals, home_team_id, away_team_id, venue_id)
    VALUES
        (1, '2010-12-15', 4, 0, 1, 2, 1),
        (2, '2005-05-15', 2, 2, 3, 2, 2),
        (3, '2020-01-01', 1, 1, 2, 1, 3);
