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

import it.tidalwave.integritychecker2.persistence.impl.springjdbc.SpringJdbcFileScan;
import it.tidalwave.integritychecker2.persistence.impl.springjdbc.SpringJdbcScan;
import it.tidalwave.integritychecker2.persistence.impl.springjdbc.SpringJdbcScanDao;
import it.tidalwave.integritychecker2.persistence.FileScan;
import it.tidalwave.integritychecker2.persistence.Scan;
import it.tidalwave.integritychecker2.persistence.ScanDao;
import it.tidalwave.role.IdFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.*;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class PersistenceIntegrationTest
  {
    private ScanDao scanDao;

    @BeforeMethod
    public void prepare()
      {
        final DataSource dataSource = createDataSource();
        final NamedParameterJdbcOperations jdbcOps = new NamedParameterJdbcTemplate(dataSource);
        final IdFactory idFactory = new MockIdFactory();
        scanDao = new SpringJdbcScanDao(jdbcOps, idFactory);

        SpringJdbcScan.createTable(jdbcOps);
        SpringJdbcFileScan.createTable(jdbcOps);
      }

    @Test
    public void must_properly_insert_a_single_Scan()
      {
        final Scan scan = scanDao.createScan(LocalDateTime.of(2015, 11, 30, 11, 42, 03));
        final List<Scan> allScans = scanDao.findAllScans();

        assertThat(allScans.size(), is(1));
        assertThat(allScans.get(0), is(scan));
      }

    @Test
    public void must_properly_insert_two_Scans()
      {
        final Scan scan1 = scanDao.createScan(LocalDateTime.of(2015, 11, 30, 11, 42, 03));
        final Scan scan2 = scanDao.createScan(LocalDateTime.of(2014, 10, 29, 10, 41, 02));
        final List<Scan> allScans = scanDao.findAllScans();

        assertThat(allScans.size(), is(2));
        assertThat(allScans.get(0), is(scan1));
        assertThat(allScans.get(1), is(scan2));
      }

    @Test
    public void must_properly_insert_ScanFiles_for_different_Scans()
      {
        final Scan scan1 = scanDao.createScan(LocalDateTime.of(2015, 11, 30, 11, 42, 03));
        final Scan scan2 = scanDao.createScan(LocalDateTime.of(2014, 10, 29, 10, 41, 02));

        final FileScan fileScan1a = scan1.createFileScan("file1a", "fp1a");
        final FileScan fileScan1b = scan1.createFileScan("file1b", "fp1b");

        final FileScan fileScan2a = scan2.createFileScan("file2a", "fp2a");
        final FileScan fileScan2b = scan2.createFileScan("file2b", "fp2b");
        final FileScan fileScan2c = scan2.createFileScan("file2c", "fp2c");

        final List<FileScan> fileScans1 = scan1.findAllFileScans();
        final List<FileScan> fileScans2 = scan2.findAllFileScans();

        assertThat(fileScans1.size(), is(2));
        assertThat(fileScans1.get(0), is(fileScan1a));
        assertThat(fileScans1.get(1), is(fileScan1b));

        assertThat(fileScans2.size(), is(3));
        assertThat(fileScans2.get(0), is(fileScan2a));
        assertThat(fileScans2.get(1), is(fileScan2b));
        assertThat(fileScans2.get(2), is(fileScan2c));
      }

    private DataSource createDataSource()
      {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .build();
      }
  }
