package com.mageddo.referrer.dataprovider.doma;

import org.seasar.doma.Column;
import org.seasar.doma.Embeddable;

@Embeddable
public record ReferrerRow(

    @Column(name = "IDT_REFERRER")
    String id,

    @Column(name = "IND_REFERRER")
    String type

) {
}
