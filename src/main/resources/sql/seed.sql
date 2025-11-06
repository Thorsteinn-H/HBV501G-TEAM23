INSERT INTO venues (venue_name, address)
VALUES
    ('Laugardalsvöllur', 'gervivegur 2'),
    ('Kópavogsvöllur', 'platstaður 5'),
    ('Þórsvöllur', 'erekki til h2');

INSERT INTO teams (team_name, is_active, team_country, venue_id)
    VALUES
        ('Þróttur', true, 'Iceland', 1),
        ('Breiðablik', true, 'Iceland', 2),
        ('Þór', true, 'Iceland', 3);

INSERT INTO players (player_name, is_active, date_of_birth, player_country, player_position, goals, team_id)
    VALUES
        ('John Snow', true,'2007-12-03', 'England', 'GOALKEEPER', 13, 1),
        ('Ariana Grande', true, '1995-08-30', 'United states of America', 'DEFENDER', 2, 2),
        ('Jón Pétur Karlsson', true, '2000-08-13', 'Iceland', 'MIDFIELDER', 9, 3);

INSERT INTO matches (match_date, home_goals, away_goals, home_team_id, away_team_id, venue_id)
    VALUES
        ('2010-12-15', 4, 0, 1, 2, 1),
        ('2005-05-15', 2, 2, 3, 2, 2),
        ('2020-01-01', 1, 1, 2, 1, 3);
