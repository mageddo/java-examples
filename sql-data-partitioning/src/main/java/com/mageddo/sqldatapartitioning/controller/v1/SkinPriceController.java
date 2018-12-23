package com.mageddo.sqldatapartitioning.controller.v1;

import com.mageddo.sqldatapartitioning.controller.converter.LocalDateConverter;
import com.mageddo.sqldatapartitioning.controller.v1.vo.SkinPriceReqV1;
import com.mageddo.sqldatapartitioning.controller.v1.vo.SkinPriceResV1;
import com.mageddo.sqldatapartitioning.service.SkinPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/skin-prices")
public class SkinPriceController {

	private final SkinPriceService skinPriceService;

	public SkinPriceController(SkinPriceService skinPriceService) {
		this.skinPriceService = skinPriceService;
	}

	@PostMapping
	public ResponseEntity create(@RequestBody SkinPriceReqV1 skinPrice){
		skinPriceService.create(skinPrice.toEntity());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{date}_{skinId}")
	public ResponseEntity get(
		@PathVariable("date") @LocalDateConverter LocalDate date,
		@PathVariable("skinId") Long id
	){
		final var skinPrice = SkinPriceResV1.valueOf(skinPriceService.find(date, id));
		if(skinPrice == null){
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(skinPrice);
	}
}
