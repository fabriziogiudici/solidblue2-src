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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
@MappedTypes(LocalDateTime.class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime>
  {
    @Override
    public void setNonNullParameter (PreparedStatement ps, int index, LocalDateTime ldt, JdbcType jdbcType)
      throws SQLException
      {
        ps.setTimestamp(index, Timestamp.valueOf(ldt));
      }

    @Override
    public LocalDateTime getNullableResult (ResultSet rs, String columnName)
      throws SQLException
      {
        return toLocalDateTime(rs.getTimestamp(columnName));
      }

    @Override
    public LocalDateTime getNullableResult (ResultSet rs, int columnIndex)
      throws SQLException
      {
        return toLocalDateTime(rs.getTimestamp(columnIndex));
      }

    @Override
    public LocalDateTime getNullableResult (CallableStatement cs, int columnIndex)
      throws SQLException
      {
        return toLocalDateTime(cs.getTimestamp(columnIndex));
      }

    private static LocalDateTime toLocalDateTime (Timestamp ts)
      {
        return (ts == null) ? null : ts.toLocalDateTime();
      }
  }
