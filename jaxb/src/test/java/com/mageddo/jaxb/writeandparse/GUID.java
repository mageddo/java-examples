package com.mageddo.jaxb.writeandparse;

import javax.xml.bind.annotation.XmlValue;
import java.util.UUID;

public class GUID {

	private final UUID uuid;

	public GUID() {
		this.uuid = UUID.randomUUID();
	}

	@XmlValue
	public String getValue(){
		return uuid.toString();
	}
}
