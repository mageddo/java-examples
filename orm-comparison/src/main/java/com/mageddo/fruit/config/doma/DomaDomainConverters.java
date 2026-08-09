package com.mageddo.fruit.config.doma;

import com.mageddo.fruit.config.doma.provider.UUIDProvider;

import org.seasar.doma.DomainConverters;

@DomainConverters({
    UUIDProvider.class
})
public class DomaDomainConverters {
}
