package pro.anuj.config;

import com.google.inject.AbstractModule;
import pro.anuj.service.DummyService;
import pro.anuj.service.DummyServiceImpl;

import javax.inject.Singleton;

class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DummyService.class).to(DummyServiceImpl.class).in(Singleton.class);
    }
}

