package com.mageddo.togglz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "feature")
@RestController
@RequestMapping("/jmx/features")
public class FeatureJmx {

	@GetMapping("/{name}/active")
	public Object isActive(@PathVariable String name) {
		return this.getFeature(name).isActive();
	}

	@PostMapping("/{name}/value")
	public void updateFeature(@PathVariable String name, @RequestParam String value) {
		FeatureSwitch.valueOf(name).setValue(value);
	}

	@GetMapping("/{name}/value")
	public Object getFeatureValue(@PathVariable String name) {
		return this.getFeature(name).getValue();
	}

	private FeatureSwitch getFeature(String name) {
		return FeatureSwitch.valueOf(name);
	}
}
