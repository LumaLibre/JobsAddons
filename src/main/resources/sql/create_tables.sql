CREATE TABLE IF NOT EXISTS claimed_command_perks(
    user_id VARCHAR(36) PRIMARY KEY NOT NULL,
    executed_commands TEXT
);