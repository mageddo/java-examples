package com.mageddo.jaxb.complexstring;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class ComplexString {

	@XmlAttribute(name = "CodErro")
	private String errCode;

	@XmlValue
	private String value;

	public String getErrCode() {
		return errCode;
	}

	public ComplexString setErrCode(String errCode) {
		this.errCode = errCode;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ComplexString setValue(String value) {
		this.value = value;
		return this;
	}
}
