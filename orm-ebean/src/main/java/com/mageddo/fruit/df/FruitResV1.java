package com.mageddo.fruit.df;

import com.mageddo.referrer.ReferrerReqV1;
import java.util.UUID;

public record FruitResV1(
    UUID id,
    String name,
    String color,
    String season,
    ReferrerReqV1 referrer
) {}
