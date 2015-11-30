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
import it.tidalwave.integritychecker2.persistence.ScanDao;
import it.tidalwave.util.Id;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class SJPersistentFileScan implements PersistentFileScan
  {
    static final String INSERT = "INSERT INTO FILE_SCAN(ID, SCAN_ID, FILE_NAME, FINGERPRINT) VALUES (:id, :scanId, :fileName, :fingerprint)";
    private static final String CREATE_TABLE = "CREATE TABLE FILE_SCAN(ID VARCHAR(36), SCAN_ID VARCHAR(36), FILE_NAME VARCHAR(200), FINGERPRINT VARCHAR(32))";

    private final ScanDao dao;

    private final SJPersistentScan scan;

    private final Id id;

    private final String fileName;

    private final String fingerprint;

    public SJPersistentFileScan (final ScanDao dao, final SJPersistentScan scan, final Id id, final String fileName, final String fingerprint)
      {
        this.dao = dao;
        this.scan = scan;
        this.id = id;
        this.fileName = fileName;
        this.fingerprint = fingerprint;
      }

    public static void createTable (final NamedParameterJdbcOperations jdbcOps)
      {
        jdbcOps.getJdbcOperations().execute(CREATE_TABLE);
      }

    public static PersistentFileScan fromResultSet (final SJScanDao dao,
                                          final SJPersistentScan scan,
                                          final ResultSet rs)
      throws SQLException
      {
        return new SJPersistentFileScan(dao,
                                      scan,
                                      new Id(rs.getString("ID")),
                                      rs.getString("FILE_NAME"),
                                      rs.getString("FINGERPRINT"));
      }

    public SqlParameterSource toSqlParameterSource()
      {
        return scan.toSqlParameterSourceForId().addValue("id", id.stringValue())
                                               .addValue("fileName", fileName)
                                               .addValue("fingerprint", fingerprint);
      }

    @Override
    public int hashCode()
      {
        return Objects.hash(id, scan, fileName, fingerprint);
      }

    @Override
    public boolean equals (final Object obj)
      {
        if ((obj == null) || (getClass() != obj.getClass()))
          {
            return false;
          }

        final SJPersistentFileScan other = (SJPersistentFileScan) obj;

        return Objects.equals(this.scan, other.scan)
            && Objects.equals(this.id, other.id)
            && Objects.equals(this.fileName, other.fileName)
            && Objects.equals(this.fingerprint, other.fingerprint);
      }

    @Override
    public String toString()
      {
        return String.format("FileScan(id: %s, fileName: %s, fingerPrint: %s", id, fileName, fingerprint);
      }
  }
