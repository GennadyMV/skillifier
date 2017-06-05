-- Add two example Courses
INSERT INTO Course (name) VALUES ('ohtu-ohpe');
INSERT INTO Course (name) VALUES ('Ohjelmoinnin jatkokurssi');

-- Add three example Weeks
-- Ohjelmoinnin perusteet
INSERT INTO Week (course, week_Number) VALUES (1, 1);
INSERT INTO Week (course, week_Number) VALUES (1, 2);
-- Ohjelmoinnin jatkokurssi
INSERT INTO Week (course, weekNumber) VALUES (2, 1);

-- Add two example Themes to Ohjelmoinnin perusteet
INSERT INTO Theme (name, week) VALUES ('viikko1', 1);
INSERT INTO Theme (name, week) VALUES ('viikko2', 2);

-- Add three example Skills to Ohjelmoinnin perusteet
INSERT INTO Skill (name, theme) VALUES ('Tekstin tulostaminen', 1);
INSERT INTO Skill (name, theme) VALUES ('Muuttujan tulostaminen', 1);
INSERT INTO Skill (name, theme) VALUES ('Ehdollinen tulostus', 2);

-- Example exercises
-- Note that Exercise name MUST MATCH the name of the .zip file
INSERT INTO Exercise (name) VALUES ('viikko1-666.AdaLoveLace.zip');
INSERT INTO Exercise (name) VALUES ('viikko1-Viikko1_011.SuurempiLuku.zip');
INSERT INTO Exercise (name) VALUES ('viikko1-Viikko1_012.IkienSumma.zip');
INSERT INTO Exercise (name) VALUES ('viikko1-Viikko1_014.PositiivinenLuku.zip');
INSERT INTO Exercise (name) VALUES ('viikko1-Viikko1_015.TaysiIkaisyys.zip');
INSERT INTO Exercise (name) VALUES ('viikko1-Viikko1_016.ParitonVaiParillinen.zip');
INSERT INTO Exercise (name) VALUES ('viikko1-Viikko1_107.AdaptiveKertolasku.zip');

-- Exercise Skills
INSERT INTO Exercise_Skill(exercise, skill) VALUES (1, 1);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (1, 2);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (2, 1);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (2, 2);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (3, 1);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (4, 1);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (5, 1);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (6, 2);
INSERT INTO Exercise_Skill(exercise, skill) VALUES (7, 2);

-- Add two example Users
INSERT INTO User (username) VALUES ('Saku');

-- Mark the first Exercise as done for Saku
INSERT INTO User_Exercise (attempted, completed, User, Exercise) VALUES (true, true, 'Saku', 1);

-- For Saku, Skillifier chooses randomly in between Skill 2 and 3
INSERT INTO User_Skill (user, skill, percentage) VALUES ('Saku', 1, 90);
INSERT INTO User_Skill (user, skill, percentage) VALUES ('Saku', 2, 30);
INSERT INTO User_Skill (user, skill, percentage) VALUES ('Saku', 3, 30);
