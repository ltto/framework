package com.tov2.framework.gdx.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.tov2.framework.core.context.annotation.Application;

@Application
public class DesktopApplication {

    public static void main(String[] args) {
//        BeanContext run = ApplicationRunner.run(DesktopApplication.class, args);
//        System.out.println(run);
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
                World world = new World(new Vector2(0, -10), true);
                System.out.println(world);
                world.step(1/60,6,2);
            }
        }, new LwjglApplicationConfiguration());
    }
}
