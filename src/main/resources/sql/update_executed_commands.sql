INSERT INTO claimed_command_perks(
    user_id, executed_commands
) VALUES (?, ?)
    ON CONFLICT(user_id) DO UPDATE SET
    executed_commands = excluded.executed_commands;