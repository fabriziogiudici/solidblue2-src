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
package it.tidalwave.integritychecker2.persistence.impl.mybatis;

import it.tidalwave.util.Id;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@MappedTypes(Id.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class IdTypeHandler extends BaseTypeHandler<Id>
  {
    @Override
    public void setNonNullParameter (PreparedStatement ps, int index, Id id, JdbcType jdbcType)
      throws SQLException
      {
        ps.setString(index, id.stringValue());
      }

    @Override
    public Id getNullableResult (ResultSet rs, String columnName)
      throws SQLException
      {
        return new Id(rs.getString(columnName));
      }

    @Override
    public Id getNullableResult (ResultSet rs, int columnIndex)
      throws SQLException
      {
        return new Id(rs.getString(columnIndex));
      }

    @Override
    public Id getNullableResult (CallableStatement cs, int columnIndex)
      throws SQLException
      {
        return new Id(cs.getString(columnIndex));
      }
  }
