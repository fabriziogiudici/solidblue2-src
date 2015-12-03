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
package it.tidalwave.integritychecker2.persistence.impl.springjdbc;

import it.tidalwave.integritychecker2.persistence.PersistentFileScan;
import it.tidalwave.role.IdFactory;
import it.tidalwave.util.Id;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import static lombok.AccessLevel.PRIVATE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@AllArgsConstructor(access = PRIVATE)
@EqualsAndHashCode(exclude = "jdbcOps")
@ToString(exclude = "jdbcOps")
class SJPersistentFileScan implements PersistentFileScan
  {
    private static final String INSERT = "INSERT INTO FILE_SCAN(ID, SCAN_ID, FILE_NAME, FINGERPRINT) "
                                                      + "VALUES(:id, :scanId, :fileName, :fingerprint)";

    private static final String SELECT = "SELECT * FROM FILE_SCAN WHERE SCAN_ID=:scanId";

    private final NamedParameterJdbcOperations jdbcOps;

    private final SJPersistentScan scan;

    private final Id id;

    private final String fileName;

    private final String fingerprint;

    SJPersistentFileScan (final NamedParameterJdbcOperations jdbcOps,
                          final IdFactory idFactory,
                          final SJPersistentScan scan,
                          final String fileName,
                          final String fingerprint)
      {
        this(jdbcOps, scan, idFactory.createId(PersistentFileScan.class), fileName, fingerprint);
      }

    static List<PersistentFileScan> selectByScan (final NamedParameterJdbcOperations jdbcOps,
                                                  final SJPersistentScan scan)
      {
        return jdbcOps.query(SELECT,
                             new MapSqlParameterSource().addValue("scanId", scan.getId().stringValue()),
                             (rs, n) -> fromResultSet(jdbcOps, scan, rs));
      }

    void insert()
      {
        jdbcOps.update(INSERT, toSqlParameters());
      }

    private static SJPersistentFileScan fromResultSet (final NamedParameterJdbcOperations jdbcOps,
                                                       final SJPersistentScan scan,
                                                       final ResultSet rs)
      throws SQLException
      {
        return new SJPersistentFileScan(jdbcOps,
                                        scan,
                                        new Id(rs.getString("ID")),
                                        rs.getString("FILE_NAME"),
                                        rs.getString("FINGERPRINT"));
      }

    private SqlParameterSource toSqlParameters()
      {
       return new MapSqlParameterSource().addValue("id", id.stringValue())
                                         .addValue("scanId", scan.getId().stringValue())
                                         .addValue("fileName", fileName)
                                         .addValue("fingerprint", fingerprint);
      }

    @Override
    public String toExportString()
      {
        return String.format("MD5(%s)=%s", fileName, fingerprint);
      }
  }
