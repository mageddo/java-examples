package com.mageddo.togglz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeatureController {

	@GetMapping("/features/my-first-job")
	public boolean myFirstJobActive() {
		return FeatureSwitch.MY_FIRST_JOB.isActive();
	}
}
