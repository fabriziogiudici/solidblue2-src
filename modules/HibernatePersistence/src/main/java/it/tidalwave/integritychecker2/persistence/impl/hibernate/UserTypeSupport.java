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

import java.io.Serializable;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.usertype.EnhancedUserType;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public abstract class UserTypeSupport implements EnhancedUserType, Serializable
  {
    private static final long serialVersionUID = -4692619064637793846L;
    
    private final Class<?> typeClass;

    private final int[] sqlTypes;

    public UserTypeSupport (final Class<?> typeClass, final int type)
      {
        this.typeClass = typeClass;
        this.sqlTypes = new int[]{ type };
      }

    @Override
    public int[] sqlTypes()
      {
        return sqlTypes;
      }

    @Override
    public Class<?> returnedClass()
      {
        return typeClass;
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
  }
