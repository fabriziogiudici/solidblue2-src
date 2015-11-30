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
package it.tidalwave.integritychecker2.persistence.impl;

import it.tidalwave.integritychecker2.persistence.FileScan;
import it.tidalwave.integritychecker2.persistence.Scan;
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
public class SpringJdbcScanDao implements ScanDao
  {
    private final NamedParameterJdbcOperations jdbcOps;

    private final IdFactory idFactory;

    public SpringJdbcScanDao (final NamedParameterJdbcOperations jdbcOps, IdFactory idFactory)
      {
        this.jdbcOps = jdbcOps;
        this.idFactory = idFactory;
      }

    @Override
    public Scan createScan (final LocalDateTime dateTime)
      {
        final SpringJdbcScan scan = new SpringJdbcScan(this, idFactory.createId(Scan.class), dateTime);
        jdbcOps.update(SpringJdbcScan.INSERT, scan.toSqlParameterSource());
        return scan;
      }

    @Override
    public FileScan createFileScan (final Scan scan, final String fileName, final String fingerprint)
      {
        final SpringJdbcFileScan fileScan = new SpringJdbcFileScan(this, ((SpringJdbcScan)scan), idFactory.createId(FileScan.class), fileName, fingerprint);
        jdbcOps.update(SpringJdbcFileScan.INSERT, fileScan.toSqlParameterSource());
        return fileScan;
      }

    @Override
    public List<Scan> findAllScans()
      {
        return jdbcOps.query(SpringJdbcScan.SELECT, (rs, rowNum) -> SpringJdbcScan.fromResultSet(this, rs));
      }

    @Override
    public List<FileScan> findFileScansIn (final Scan scan)
      {
        return jdbcOps.query("SELECT * FROM FILE_SCAN WHERE SCAN_ID=:scanId",
                             ((SpringJdbcScan)scan).toSqlParameterSourceForId(),
                             (rs, rowNum) -> SpringJdbcFileScan.fromResultSet(this, ((SpringJdbcScan)scan), rs));
      }
  }