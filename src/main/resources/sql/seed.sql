TRUNCATE TABLE matches RESTART IDENTITY CASCADE;
TRUNCATE TABLE players RESTART IDENTITY CASCADE;
TRUNCATE TABLE teams RESTART IDENTITY CASCADE;
TRUNCATE TABLE venues RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

INSERT INTO venues (venue_name, address)
VALUES
    ('Laugardalsvöllur', 'gervivegur 2'),
    ('Kópavogsvöllur', 'platstaður 5'),
    ('Þórsvöllur', 'er ekki til h2');

INSERT INTO teams (team_name, is_active, team_country, venue_id)
VALUES
    ('Þróttur', true, 'IS', 1),
    ('Breiðablik', true, 'IS', 2),
    ('Þór', true, 'IS', 3);

INSERT INTO players (player_name, is_active, date_of_birth, player_country, player_position, goals, team_id)
VALUES
    ('John Snow', true,'2007-12-03', 'UK', 'GOALKEEPER', 13, 1),
    ('Ariana Grande', true, '1995-08-30', 'US', 'DEFENDER', 2, 2),
    ('Jón Pétur Karlsson', true, '2000-08-13', 'IS', 'MIDFIELDER', 9, 3);

INSERT INTO matches (match_date, home_goals, away_goals, home_team_id, away_team_id, venue_id)
VALUES
    ('2010-12-15', 4, 0, 1, 2, 1),
    ('2005-05-15', 2, 2, 3, 2, 2),
    ('2020-01-01', 1, 1, 2, 1, 3);

INSERT INTO users (email, user_name, gender, password_hash, role)
VALUES
    ('admin@admin.com', 'admin', 'Female', '$2b$10$C5UxG6mQC3UUCKo4nRQy4e.Vq.fiMTat5NNfg/xrZE8o55tNsWYUq', 'ADMIN'),
    ('user@user.com', 'user', 'Male', '$2b$10$An12EyO/VUKgltrb9EGIKe6BVI28vnuNzcHL5qa7GZb5GGitboXX2', 'User');
