package com.tov2.framework.gdx.desktop.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tov2.framework.core.context.annotation.Component;
import com.tov2.framework.core.life.ApplicationStarter;
import com.tov2.framework.gdx.desktop.game.model.AbstractGameObject;
import lombok.Data;

@Data
@Component
public class GlobalConfig implements ApplicationStarter {
    private String assetPrefix = "123";

    private volatile Stage mainStage;

    @Override
    public int startOrder() {
        return -99999;
    }

    @Override
    public void startup() {
        AbstractGameObject actor = new AbstractGameObject();
        Gdx.app.postRunnable(() -> {
            mainStage = new Stage();
            mainStage.addActor(actor);
        });
    }
}
