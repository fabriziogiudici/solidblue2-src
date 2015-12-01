/*
 * #%L
 * %%
 * %%
 * #L%
 */
package it.tidalwave.integritychecker2.persistence.impl.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.EnhancedUserType;


/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class LocalDateTimeUserType implements EnhancedUserType, Serializable
  {
    private static final int[] SQL_TYPES = new int[]{Types.TIMESTAMP};

    @Override
    public int[] sqlTypes()
      {
        return SQL_TYPES;
      }

    @Override
    public Class returnedClass()
      {
        return LocalDateTime.class;
      }

    @Override
    public boolean equals (final Object o1, final Object o2)
      throws HibernateException
      {
        return Objects.equals(o1, o2);
      }

    @Override
    public int hashCode (final Object object)
      throws HibernateException
      {
        return object.hashCode();
      }

    @Override
    public Object nullSafeGet (ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
      throws HibernateException, SQLException
      {
        Object timestamp = StandardBasicTypes.TIMESTAMP.nullSafeGet(resultSet, names, session, owner);

        if (timestamp == null)
          {
            return null;
          }

        final Date ts = (Date) timestamp;
        final Instant instant = Instant.ofEpochMilli(ts.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
      }

    @Override
    public void nullSafeSet (PreparedStatement preparedStatement, Object value, int index, SessionImplementor session)
      throws HibernateException, SQLException
      {
        if (value == null)
          {
            StandardBasicTypes.TIMESTAMP.nullSafeSet(preparedStatement, null, index, session);
          }
        else
          {
            final LocalDateTime ldt = ((LocalDateTime) value);
            final Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
            final Date timestamp = Date.from(instant);
            StandardBasicTypes.TIMESTAMP.nullSafeSet(preparedStatement, timestamp, index, session);
          }
      }

    @Override
    public Object deepCopy (Object value)
      {
        return value;
      }

    @Override
    public boolean isMutable()
      {
        return false;
      }

    @Override
    public Serializable disassemble (Object value)
      {
        return (Serializable) value;
      }

    @Override
    public Object assemble (Serializable cached, Object value)
      throws HibernateException
      {
        return cached;
      }

    @Override
    public Object replace (Object original, Object target, Object owner)
      throws HibernateException
      {
        return original;
      }

    @Override
    public String objectToSQLString(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toXMLString (Object object)
      {
        return object.toString();
      }

    @Override
    public Object fromXMLString (String string)
      {
        return LocalDateTime.parse(string);
      }
  }
