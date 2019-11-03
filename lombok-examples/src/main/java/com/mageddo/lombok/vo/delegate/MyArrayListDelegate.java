package com.mageddo.lombok.vo.delegate;

import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyArrayListDelegate implements List<String> {

	@Getter
	private int addItems = 0;

	@Delegate(excludes = ListAdd.class)
	private List<String> delegate = new ArrayList<>();

	@Override
	public boolean add(String e){
		this.addItems++;
		return this.delegate.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		this.addItems += c.size();
		return this.delegate.addAll(c);
	}

	private interface ListAdd {
		boolean add(String e);
		boolean addAll(Collection<? extends String> c);
	}
}
