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

import it.tidalwave.integritychecker2.FileAndFingerprint;
import it.tidalwave.integritychecker2.model.Scan;
import it.tidalwave.integritychecker2.persistence.ImportController;
import it.tidalwave.integritychecker2.persistence.Persistence;
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import it.tidalwave.integritychecker2.persistence.ScanRepository;
import static it.tidalwave.util.test.FileComparisonUtils.assertSameContents;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public abstract class PersistenceIntegrationTestSupport
  {
    protected ScanRepository scanRepository;

    protected Persistence persistence;

    protected ImportController importController;

    @AfterMethod
    public void cleanup()
      throws SQLException
      {
        persistence.scratch();
      }

    @Test
    public void must_properly_insert_a_single_Scan()
      {
        final PersistentScan scan = scanRepository.createScan(LocalDateTime.of(2015, 11, 30, 11, 42, 03));
        final List<PersistentScan> allScans = scanRepository.findAllScans();

        assertThat(allScans.size(), is(1));
        assertThat(allScans.get(0), is(scan));
      }

    @Test(dependsOnMethods = "must_properly_insert_a_single_Scan")
    public void must_properly_insert_two_Scans()
      {
        final PersistentScan scan1 = scanRepository.createScan(LocalDateTime.of(2015, 11, 30, 11, 42, 03));
        final PersistentScan scan2 = scanRepository.createScan(LocalDateTime.of(2014, 10, 29, 10, 41, 02));
        final List<PersistentScan> allScans = scanRepository.findAllScans();

        assertThat(allScans.size(), is(2));
        assertThat(allScans.get(0), is(scan1));
        assertThat(allScans.get(1), is(scan2));
      }

    @Test(dependsOnMethods = "must_properly_insert_two_Scans")
    public void must_properly_store_and_reload_stuff()
      {
        final Scan scan1 = Scan.builder().creationDateTime(LocalDateTime.of(2015, 11, 30, 11, 42, 03))
                                .build()
                                .with(new FileAndFingerprint(Paths.get("file1a"), "fp1a"))
                                .with(new FileAndFingerprint(Paths.get("file1b"), "fp1b"));
        final Scan scan2 = Scan.builder().creationDateTime(LocalDateTime.of(2014, 10, 29, 10, 41, 02))
                                .build()
                                .with(new FileAndFingerprint(Paths.get("file2a"), "fp2a"))
                                .with(new FileAndFingerprint(Paths.get("file2b"), "fp2b"))
                                .with(new FileAndFingerprint(Paths.get("file2c"), "fp2c"));
        scan1.storeTo(scanRepository);
        scan2.storeTo(scanRepository);
        final List<Scan> allScans = Scan.loadAllFrom(scanRepository);

        assertThat(allScans.get(0), is(scan1));
        assertThat(allScans.get(1), is(scan2));
      }

    @Test(dependsOnMethods = "must_properly_store_and_reload_stuff")
    public void must_properly_import_and_export()
      throws IOException
      {
        final Path expectedFile = Paths.get("../Persistence/target/test-classes/fingerprints-20151112_1449.txt"); // FIXME
        final Path actualFile = Paths.get("target/fingerprints-exported.txt");

        final Scan scan = importController.importFile(LocalDateTime.of(2015, 11, 30, 11, 42, 03), expectedFile);
        scan.storeTo(scanRepository);
        Scan.loadAllFrom(scanRepository).get(0).export(actualFile);

        assertSameContents(expectedFile.toFile(), actualFile.toFile());
      }
  }
