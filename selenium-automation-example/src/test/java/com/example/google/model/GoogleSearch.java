package com.example.google.model;


import java.util.List;

import com.example.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by elvis on 27/08/16.
 */
public class GoogleSearch extends Page {
	/**
	 * Constructor
	 *
	 * @param driver
	 */
	public GoogleSearch(WebDriver driver) {
		super(driver);
	}

	@Override
	public String getTitle() {
		return "Hy";
	}

	public int search(String word){
		driver.get("https://www.google.com.br/search?site=&source=hp&q=" + word);
		List<WebElement> userField = driver.findElements(By.id("rso"));
		return userField.size();
	}
}
