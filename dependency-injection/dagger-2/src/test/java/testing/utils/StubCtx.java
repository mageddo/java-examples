package testing.utils;

import javax.inject.Singleton;

import com.mageddo.main.FruitDAOMock;
import com.mageddo.main.config.Ctx;

import dagger.Component;


@Singleton
@Component(
    modules = {
        StubMainModule.class
    }
)
public interface StubCtx extends Ctx {

  static StubCtx create() {
    return DaggerStubCtx.create();
  }

  FruitDAOMock fruitDao();
}
