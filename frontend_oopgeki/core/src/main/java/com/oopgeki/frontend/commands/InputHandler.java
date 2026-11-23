package com.oopgeki.frontend.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {
    private int keyMoveLeft = Input.Keys.LEFT;
    private int keyMoveRight = Input.Keys.RIGHT;
    private int keyBeat1 = Input.Keys.Q;
    private int keyBeat2 = Input.Keys.E;

    private MoveCommand moveCommand = new MoveCommand(0);

    private final List<Command> activeCommands = new ArrayList<>();

    public InputHandler() {}

    public List<Command> handleInput(float delta) {
        activeCommands.clear();

        if (Gdx.input == null) {
            return activeCommands;
        }

        float amount = 0;

        // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            amount--;
        }

        // Move right
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            amount++;
        }

        if (amount != 0) {
            moveCommand.setDirection(amount);
            activeCommands.add(moveCommand);
        }

        return activeCommands;
    }
}
