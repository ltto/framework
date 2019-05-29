package com.tov2.framework.gdx.desktop.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tov2.framework.core.context.annotation.Component;
import com.tov2.framework.core.life.ApplicationStarter;
import com.tov2.framework.gdx.desktop.config.GlobalConfig;

import javax.annotation.Resource;

@Component
public class GameApplicationStarter extends Game implements ApplicationStarter {

    @Resource
    private LwjglApplicationConfiguration lwjglApplicationConfiguration;
    @Resource
    private Screen screen;
    @Resource
    private GlobalConfig globalConfig;

    @Override
    public int startOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void startup() {
        new LwjglApplication(this, lwjglApplicationConfiguration);
        System.out.println("启动成功");

    }

    @Override
    public void create() {
        this.setScreen(screen);
        System.out.println("创建完成");
    }

}
