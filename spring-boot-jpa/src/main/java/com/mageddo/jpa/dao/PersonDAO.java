package com.mageddo.jpa.dao;

import com.mageddo.jpa.entity.Person;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/30/17 2:55 PM
 */
public interface PersonDAO {

	Person save(Person person);
	Person find(int id);
}
