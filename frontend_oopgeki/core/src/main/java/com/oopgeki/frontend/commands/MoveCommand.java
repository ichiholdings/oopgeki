package com.oopgeki.frontend.commands;

import com.oopgeki.frontend.Player;

public class MoveCommand implements Command {
    private float direction;

    public MoveCommand(float direction) {
        this.direction = direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Player player, float delta) {
        player.move(direction, delta);
    }
}
