package com.mageddo.jpa.dao;

import com.mageddo.jpa.entity.Car;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/30/17 3:19 PM
 */
public interface CarDAO {

	Car save(Car car);
	Car find(String carID);

}
