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
 * $Id: ProgressTracker.java,v 91dd9dc0d25a 2015/11/03 20:25:03 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: ProgressTracker.java,v 91dd9dc0d25a 2015/11/03 20:25:03 fabrizio $
 *
 **********************************************************************************************************************/
public class ProgressTracker
  {
    private static final Logger log = LoggerFactory.getLogger(ProgressTracker.class);

    private final AtomicInteger discoveryCount = new AtomicInteger();
    private final AtomicInteger scanCount = new AtomicInteger();
    private final AtomicLong discoverySize = new AtomicLong();
    private final AtomicLong scanSize = new AtomicLong();

    /*******************************************************************************************************************
     *
     * Updates the statistics for a freshly discovered file.
     *
     * @param   file            the file
     *
     ******************************************************************************************************************/
    public void notifyDiscoveredFile (final Path file)
      {
        try
          {
            log.info("Discovered {}", file.getFileName());
            discoveryCount.incrementAndGet();
            discoverySize.accumulateAndGet(Files.size(file), Long::sum);
            logProgress();
          }
        catch (IOException e)
          {
            log.warn("", e);
          }
      }

    /*******************************************************************************************************************
     *
     * Updates the statistics for a freshly scanned file.
     *
     * @param   file            the file
     *
     ******************************************************************************************************************/
    public void notifyScannedFile (final Path file)
      {
        try
          {
            scanCount.incrementAndGet();
            scanSize.accumulateAndGet(Files.size(file), Long::sum);
            logProgress();
          }
        catch (IOException e)
          {
            log.warn("", e);
          }
      }

    /*******************************************************************************************************************
     *
     * Logs the current progress.
     *
     ******************************************************************************************************************/
    public void logProgress()
      {
        final int sc = scanCount.get();
        final int dc = discoveryCount.get();
        final long ss = scanSize.get();
        final long ds = discoverySize.get();
        log.info("{}", String.format("Processed files: %d/%d (%d%%) - size: %dMB/%dMB (%d%%)",
                                     sc, dc, (100 * sc / dc),
                                     ss / 1_000_000, ds / 1_000_000, (100 * ss / ds)));
      }
  }