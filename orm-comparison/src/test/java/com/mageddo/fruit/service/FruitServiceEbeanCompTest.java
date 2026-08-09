package com.mageddo.fruit.service;

import com.mageddo.persistence.OrmProvider;
import com.mageddo.testing.DatabaseConfiguratorExtension;
import com.mageddo.testing.orm.EbeanTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DatabaseConfiguratorExtension.class)
@QuarkusTest
@TestProfile(EbeanTestProfile.class)
class FruitServiceEbeanCompTest extends FruitServiceCompTest {

  @Override
  OrmProvider expectedOrm() {
    return OrmProvider.EBEAN;
  }
}
