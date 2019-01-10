package com.mageddo.jpa.lazycolumnload.dao;

import com.mageddo.jpa.entity.Person;

public interface PersonDAO {
	Person save(Person person);
	Person find(int id);
}
