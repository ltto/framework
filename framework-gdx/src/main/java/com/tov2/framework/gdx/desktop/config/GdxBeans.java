package com.tov2.framework.gdx.desktop.config;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tov2.framework.core.context.annotation.Bean;
import com.tov2.framework.core.context.annotation.Component;

@Component
public class GdxBeans {

    @Bean
    public LwjglApplicationConfiguration config() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "demo";
        return configuration;
    }

}
