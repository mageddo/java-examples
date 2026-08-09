package com.mageddo.fruit.dataprovider.doma;

import org.seasar.doma.Column;
import org.seasar.doma.Embeddable;

@Embeddable
public record FruitDomaReferrerRow(
    @Column(name = "IDT_REFERRER") String id,
    @Column(name = "IND_REFERRER") String type
) {
}
