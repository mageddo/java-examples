package com.mageddo.lombok.vo.delegate;

import lombok.experimental.Delegate;

public class DoorDelegate implements Door {

	@Delegate(types = Door.class, excludes = DoorCloser.class)
	private final DefaultDoor delegate = new DefaultDoor();

	@Override
	public void close() {
		System.out.println("delegating door closing");
		delegate.close();
	}

	private interface DoorCloser {
		void close();
	}
}
