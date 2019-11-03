package com.mageddo.lombok.vo.delegate;

public class DefaultDoor implements Door {

	@Override
	public void open() {
		System.out.println("door was opened");
	}

	@Override
	public void close() {
		System.out.println("the door was closed");
	}
}
