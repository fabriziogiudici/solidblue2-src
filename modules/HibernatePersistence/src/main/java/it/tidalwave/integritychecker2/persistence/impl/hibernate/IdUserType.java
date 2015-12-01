/*
 * #%L
 * *********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * http://solidblue.tidalwave.it - hg clone https://bitbucket.org/tidalwave/solidblue2-src
 * %%
 * Copyright (C) 2015 - 2015 Tidalwave s.a.s. (http://tidalwave.it)
 * %%
 * *********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * *********************************************************************************************************************
 *
 * $Id: Main.java,v b4f706516290 2015/11/07 08:47:17 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2.persistence.impl.hibernate;

import it.tidalwave.util.Id;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class IdUserType implements EnhancedUserType, Serializable
  {
    private static final int[] SQL_TYPES = new int[]{ Types.VARCHAR };

    @Override
    public int[] sqlTypes()
      {
        return SQL_TYPES;
      }

    @Override
    public Class returnedClass()
      {
        return Id.class;
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
        Object id = StandardBasicTypes.STRING.nullSafeGet(resultSet, names, session, owner);

        return (id == null) ? null : new Id((String)id);
      }

    @Override
    public void nullSafeSet (PreparedStatement preparedStatement, Object value, int index, SessionImplementor session)
      throws HibernateException, SQLException
      {
        if (value == null)
          {
            StandardBasicTypes.STRING.nullSafeSet(preparedStatement, null, index, session);
          }
        else
          {
            StandardBasicTypes.STRING.nullSafeSet(preparedStatement, ((Id)value).stringValue(), index, session);
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
        return (Serializable)value;
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
    public String objectToSQLString(Object object)
      {
        throw new UnsupportedOperationException();
      }

    @Override
    public String toXMLString (Object object)
      {
        return ((Id)object).stringValue();
      }

    @Override
    public Object fromXMLString (String string)
      {
        return new Id(string);
      }
  }
