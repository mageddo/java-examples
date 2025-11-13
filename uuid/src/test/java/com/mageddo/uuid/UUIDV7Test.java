package com.mageddo.uuid;

import com.fasterxml.uuid.Generators;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UUIDV7Test {

  public static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

  @Test
  void mustParse(){

    final var id = Generators.timeBasedEpochRandomGenerator().generate();

    assertEquals(7, id.version());
  }

  @Test
  void mustBeNaturallySortedByTimestamp(){

    final var ids = IntStream.range(0, 100)
        .boxed()
        .map(it -> Generators.timeBasedEpochRandomGenerator().generate())
        .toList();

    final var sortedIds = new ArrayList<>(ids);
    sortedIds.sort(Comparator.comparing(UuidV7Utils::extractEpochMillis));

    assertEquals(ids, sortedIds);
  }

  @Test
  void mustExtractTimestampFromUuidCloseToNow(){

    final var uuid = Generators.timeBasedEpochRandomGenerator().generate();

    final var ref = UuidV7Utils.toLocalDateTime(uuid, ZONE_ID);

    assertThat(ref)
        .isCloseTo(LocalDateTime.now(ZONE_ID), within(Duration.ofSeconds(5)));

  }
}
