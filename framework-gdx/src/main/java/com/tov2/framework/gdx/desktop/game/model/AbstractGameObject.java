package com.tov2.framework.gdx.desktop.game.model;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class AbstractGameObject extends Actor {
    public AbstractGameObject() {
    }

    @Override
    public void act(float delta) {
        System.out.println(delta);
    }
}
