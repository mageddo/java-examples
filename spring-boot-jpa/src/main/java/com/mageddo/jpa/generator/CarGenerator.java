package com.mageddo.jpa.generator;

import com.mageddo.jpa.entity.Car;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @author elvis
 * @version $Revision: $<br/>
 *          $Id: $
 * @since 8/30/17 3:17 PM
 */
public class CarGenerator implements IdentifierGenerator {
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		final Car entity = (Car) object;
		return entity.getModel() + '-' + entity.getYear();
	}
}
