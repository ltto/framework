package com.tov2.framework.gdx.desktop.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tov2.framework.core.context.annotation.Component;
import com.tov2.framework.gdx.desktop.config.GlobalConfig;
import lombok.Data;

import javax.annotation.Resource;

@Data
@Component()
public class DefaultScreen implements Screen {

    @Resource
    private GlobalConfig globalConfig;

    private Stage stage;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage == null) {
            stage = globalConfig.getMainStage();
        }
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
