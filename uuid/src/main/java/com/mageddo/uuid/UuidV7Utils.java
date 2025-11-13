package com.mageddo.uuid;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public final class UuidV7Utils {


  private UuidV7Utils() {
  }

  /**
   * Extrai o timestamp (epoch ms) dos 48 bits superiores de um UUID v7.
   */
  public static long extractEpochMillis(UUID uuid) {
    if (uuid.version() != 7) {
      throw new IllegalArgumentException("UUID não é versão 7: " + uuid);
    }
    // MSB (64 bits). Layout v7:
    // [ 48 bits timestamp ][ 4 bits versão=7 ][ 12 bits rand_a ]
    // Desloca 16 bits p/ descartar versão+rand_a e mascara 48 bits restantes.
    long msb = uuid.getMostSignificantBits();
    return (msb >>> 16) & 0x0000_FFFF_FFFF_FFFFL;
  }

  /**
   * Converte para LocalDateTime no fuso informado.
   */
  public static LocalDateTime toLocalDateTime(UUID uuid, ZoneId zone) {
    long epochMs = extractEpochMillis(uuid);
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMs), zone);
  }

  /**
   * Atalho: retorna em UTC.
   */
  public static LocalDateTime toLocalDateTimeUtc(UUID uuid) {
    return toLocalDateTime(uuid, ZoneId.of("UTC"));
  }
}
