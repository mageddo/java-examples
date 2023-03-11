package testing.utils;

import javax.inject.Singleton;

import com.mageddo.main.FruitDAO;
import com.mageddo.main.FruitDAOMock;

import dagger.Binds;
import dagger.Module;

@Module
public interface StubMainModule  {
  @Binds
  @Singleton
  FruitDAO bind(FruitDAOMock impl);
}
