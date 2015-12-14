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
package it.tidalwave.integritychecker2.impl;

import it.tidalwave.integritychecker2.FileAndFingerprint;
import it.tidalwave.integritychecker2.ProgressTracker;
import it.tidalwave.integritychecker2.ui.IntegrityCheckerFieldsBean;
import it.tidalwave.integritychecker2.ui.IntegrityCheckerPresentation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: ProgressTracker.java,v 91dd9dc0d25a 2015/11/03 20:25:03 fabrizio $
 *
 **********************************************************************************************************************/
public class DefaultProgressTracker implements ProgressTracker
  {
    private static final Logger log = LoggerFactory.getLogger(DefaultProgressTracker.class);

    private final AtomicInteger discoveryCount = new AtomicInteger();
    private final AtomicInteger scanCount = new AtomicInteger();
    private final AtomicLong discoverySize = new AtomicLong();
    private final AtomicLong scanSize = new AtomicLong();

    private final IntegrityCheckerFieldsBean fields = new IntegrityCheckerFieldsBean();

    private final IntegrityCheckerPresentation presentation;

    public DefaultProgressTracker (final IntegrityCheckerPresentation presentation)
      {
        this.presentation = presentation;
        presentation.bind(fields);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
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
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void notifyScannedFile (final FileAndFingerprint faf)
      {
        try
          {
            scanCount.incrementAndGet();
            scanSize.accumulateAndGet(Files.size(faf.getFile()), Long::sum);
            logProgress();
          }
        catch (IOException e)
          {
            log.warn("", e);
          }
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void logProgress()
      {
        final int sc = scanCount.get();
        final int dc = discoveryCount.get();
        final long ss = scanSize.get();
        final long ds = discoverySize.get();
        log.info("{}", String.format("Processed files: %d/%d (%d%%) - size: %dMB/%dMB (%d%%)",
                                     sc, dc, (100 * sc / dc),
                                     ss / 1_000_000, ds / 1_000_000, (100 * ss / ds)));
        Platform.runLater(() -> // FIXME: remove Platform from here
          {
            fields.processedProperty().set(String.format("%d/%d", sc, dc));
            fields.totalProperty().set(String.format("%dMB/%dMB", ss / 1_000_000, ds / 1_000_000));
            fields.progressProperty().set((float)ss / ds);
          });
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void close()
      throws Exception
      {
        presentation.dispose();
      }
  }
