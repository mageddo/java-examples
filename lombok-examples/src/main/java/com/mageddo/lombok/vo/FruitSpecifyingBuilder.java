package com.mageddo.lombok.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = FruitSpecifyingBuilder.FruitSpecifyingBuilderBuilder.class)
public class FruitSpecifyingBuilder {

	@NonNull
	private final String name;

	@JsonPOJOBuilder(withPrefix = "")
	public static final class FruitSpecifyingBuilderBuilder {
	}

}
