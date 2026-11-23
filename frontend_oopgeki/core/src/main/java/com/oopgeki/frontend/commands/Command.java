package com.oopgeki.frontend.commands;

import com.oopgeki.frontend.Player;

public interface Command {
    void execute(Player player, float delta);
}

