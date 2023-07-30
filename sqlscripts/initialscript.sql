CREATE TABLE users (
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY ,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    display_name VARCHAR(150)
);
CREATE TABLE groups (
    group_id INTEGER PRIMARY KEY,
    group_name TEXT NOT NULL
);
CREATE TABLE group_members (
    group_id INTEGER,
    user_id INTEGER,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES groups(group_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE messages (
    message_id INTEGER PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    group_id INTEGER,
    content TEXT NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (group_id) REFERENCES groups(group_id)
);
-- add random user accounts
INSERT INTO users ( username, password, display_name)
VALUES ('Octavia', '123456', 'John Foe');


-- Drop all tables

DROP TABLE users;

DROP TABLE groups;

DROP TABLE group_members;

DROP TABLE messages;