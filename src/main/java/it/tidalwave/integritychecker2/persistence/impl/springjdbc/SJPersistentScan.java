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
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import it.tidalwave.role.IdFactory;
import it.tidalwave.util.Id;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class SJPersistentScan implements PersistentScan
  {
    private static final String SELECT = "SELECT * FROM SCAN";
    private static final String INSERT = "INSERT INTO SCAN(ID, CREATION_TIME) VALUES(:id, :creationTime)";

    private final IdFactory idFactory;

    private final NamedParameterJdbcOperations jdbcOps;

    private final Id id;

    private final LocalDateTime creationDateTime;

    public static void createTable (final Statement statement)
      throws SQLException
      {
        statement.execute(CREATE_TABLE);
      }

    SJPersistentScan (final NamedParameterJdbcOperations jdbcOps,
                      final IdFactory idFactory,
                      final Id id,
                      final LocalDateTime dateTime)
      {
        this.jdbcOps = jdbcOps;
        this.idFactory = idFactory;
        this.id = id;
        this.creationDateTime = dateTime;
      }

    static List<PersistentScan> selectAll (final NamedParameterJdbcOperations jdbcOps,
                                           final IdFactory idFactory)
      {
        return jdbcOps.query(SELECT, (rs, rowNum) -> fromResultSet(jdbcOps, idFactory, rs));
      }

    @Override
    public PersistentFileScan createFileScan (final String fileName, final String fingerprint)
      {
        final SJPersistentFileScan fileScan = new SJPersistentFileScan(jdbcOps,
                                                                       this,
                                                                       idFactory.createId(PersistentFileScan.class),
                                                                       fileName,
                                                                       fingerprint);
        fileScan.insert();
        return fileScan;
      }

    @Override
    public List<PersistentFileScan> findAllFileScans()
      {
        return SJPersistentFileScan.selectByScan(jdbcOps, idFactory, this);
      }

    void insert()
      {
        jdbcOps.update(INSERT,
                       new MapSqlParameterSource().addValue("id", id.stringValue())
                                                  .addValue("creationTime", Timestamp.valueOf(creationDateTime)));
      }

    @Override
    public int hashCode()
      {
        return Objects.hash(id, creationDateTime);
      }

    @Override
    public boolean equals (final Object obj)
      {
        if ((obj == null) || (getClass() != obj.getClass()))
          {
            return false;
          }

        final SJPersistentScan other = (SJPersistentScan)obj;

        return Objects.equals(this.id, other.id)
            && Objects.equals(this.creationDateTime, other.creationDateTime);
      }

    @Override
    public String toString()
      {
        return String.format("Scan(id: %s, creationDateTime: %s", id, creationDateTime);
      }

    MapSqlParameterSource toSqlParameterSourceForId()
      {
        return new MapSqlParameterSource().addValue("scanId", id.stringValue());
      }

    private static PersistentScan fromResultSet (final NamedParameterJdbcOperations jdbcOps,
                                                 final IdFactory idFactory,
                                                 final ResultSet rs)
      throws SQLException
      {
        return new SJPersistentScan(jdbcOps,
                                    idFactory,
                                    new Id(rs.getString("ID")),
                                    rs.getTimestamp("CREATION_TIME").toLocalDateTime());
      }
  }
