package com.oopgeki.frontend.observers;

public interface GameObserver {
    void onScoreChanged(long newScore);
    void onHpChanged(int currentHp);
    default void onLivesChanged(int currentLives) {}
}

