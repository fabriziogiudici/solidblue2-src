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
import it.tidalwave.integritychecker2.persistence.ScanDao;
import it.tidalwave.role.IdFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class SJScanDao implements ScanDao
  {
    private final NamedParameterJdbcOperations jdbcOps;

    private final IdFactory idFactory;

    public SJScanDao (final NamedParameterJdbcOperations jdbcOps, final IdFactory idFactory)
      {
        this.jdbcOps = jdbcOps;
        this.idFactory = idFactory;
      }

    @Override
    public PersistentScan createScan (final LocalDateTime dateTime)
      {
        final SJPersistentScan scan = new SJPersistentScan(this, idFactory.createId(PersistentScan.class), dateTime);
        jdbcOps.update(SJPersistentScan.INSERT, scan.toSqlParameterSource());
        return scan;
      }

    @Override
    public PersistentFileScan createFileScan (final PersistentScan scan, final String fileName, final String fingerprint)
      {
        final SJPersistentFileScan fileScan = new SJPersistentFileScan(this, ((SJPersistentScan)scan), idFactory.createId(PersistentFileScan.class), fileName, fingerprint);
        jdbcOps.update(SJPersistentFileScan.INSERT, fileScan.toSqlParameterSource());
        return fileScan;
      }

    @Override
    public List<PersistentScan> findAllScans()
      {
        return jdbcOps.query(SJPersistentScan.SELECT, (rs, rowNum) -> SJPersistentScan.fromResultSet(this, rs));
      }

    @Override
    public List<PersistentFileScan> findFileScansIn (final PersistentScan scan)
      {
        return jdbcOps.query("SELECT * FROM FILE_SCAN WHERE SCAN_ID=:scanId",
                             ((SJPersistentScan)scan).toSqlParameterSourceForId(),
                             (rs, rowNum) -> SJPersistentFileScan.fromResultSet(this, ((SJPersistentScan)scan), rs));
      }
  }
