package br.com.db1.converter;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;

public class LocalDateTimeUserType implements EnhancedUserType {

	private static final int[] SQL_TYPES = new int[] { Types.TIMESTAMP };

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class<LocalDateTime> returnedClass() {
		return LocalDateTime.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		}
		if (x == null || y == null) {
			return false;
		}
		LocalDateTime dtx = (LocalDateTime) x;
		LocalDateTime dty = (LocalDateTime) y;
		return dtx.equals(dty);
	}

	public int hashCode(Object object) throws HibernateException {
		return object.hashCode();
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object timestamp = StandardBasicTypes.TIMESTAMP.nullSafeGet(resultSet, names, session, owner);
		if (timestamp == null) {
			return null;
		}
		Date ts = (Date) timestamp;
		Instant instant = Instant.ofEpochMilli(ts.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.of("America/Sao_Paulo"));
	}

	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			StandardBasicTypes.TIMESTAMP.nullSafeSet(preparedStatement, null, index, session);
		} else {
			LocalDateTime ldt = ((LocalDateTime) value);
			Instant instant = ldt.atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
			Date timestamp = Date.from(instant);
			StandardBasicTypes.TIMESTAMP.nullSafeSet(preparedStatement, timestamp, index, session);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object value) throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	public String objectToSQLString(Object object) {
		throw new UnsupportedOperationException();
	}

	public String toXMLString(Object object) {
		return object.toString();
	}

	public Object fromXMLString(String string) {
		return LocalDateTime.parse(string);
	}

}
