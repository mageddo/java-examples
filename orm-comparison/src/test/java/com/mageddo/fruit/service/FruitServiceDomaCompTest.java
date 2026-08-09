package com.mageddo.fruit.service;

import com.mageddo.persistence.OrmProvider;
import com.mageddo.testing.DatabaseConfiguratorExtension;
import com.mageddo.testing.orm.DomaTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DatabaseConfiguratorExtension.class)
@QuarkusTest
@TestProfile(DomaTestProfile.class)
class FruitServiceDomaCompTest extends FruitServiceCompTest {

  @Override
  OrmProvider expectedOrm() {
    return OrmProvider.DOMA;
  }
}
