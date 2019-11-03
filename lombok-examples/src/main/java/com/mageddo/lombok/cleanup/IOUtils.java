package com.mageddo.lombok.cleanup;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@UtilityClass
public class IOUtils {

	@SneakyThrows
	public String readToStringAndClose(InputStream in) {
		@Cleanup val in0 = in;
		val bout = new ByteArrayOutputStream();
		val buff = new byte[512];
		for(int i; ; ){
			i = in0.read(buff);
			if(i == -1) {
				return bout.toString();
			}
			bout.write(buff, 0, i);
		}
	}
}
