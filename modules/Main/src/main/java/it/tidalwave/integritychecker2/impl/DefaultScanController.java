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
import it.tidalwave.integritychecker2.Main;
import it.tidalwave.integritychecker2.ProgressTracker;
import it.tidalwave.integritychecker2.ScanController;
import it.tidalwave.integritychecker2.ui.IntegrityCheckerPresentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class DefaultScanController implements ScanController
  {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final List<String> FILE_EXTENSIONS = Arrays.asList("nef", "arw", "dng", "tif", "jpg");

    private final IntegrityCheckerPresentation presentation;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public DefaultScanController (final IntegrityCheckerPresentation presentation)
      {
        this.presentation = presentation;
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void scan (final Path targetPath)
      {
        try
          {
            log.info("Scanning {}...", targetPath);

            try (final Stream<Path> stream = Files.walk(targetPath, FOLLOW_LINKS);
                 final FileStorage storage = new FileStorage(targetPath);
                 final ProgressTracker progressTracker = new DefaultProgressTracker(presentation))
              {
                stream.filter(DefaultScanController::matchesExtension)
                      .peek(progressTracker::notifyDiscoveredFile)
                      .collect(storage.getIntermediateCollector())
                      .stream()
                      .map(FileAndFingerprint::new)
                      .peek(progressTracker::notifyScannedFile)
                      .collect(storage.getFinalCollector());
              }
          }
        catch (Exception e)
          {
            log.error("", e);
          }
      }

    /*******************************************************************************************************************
     *
     * Filters files with the supported extensions.
     *
     * @param   file            the file
     * @return                  {@code true} if the file matches
     *
     ******************************************************************************************************************/
    private static boolean matchesExtension (final Path file)
      {
        final String extension = file.getFileName().toString().replaceAll("^.*\\.", "").toLowerCase();
        return Files.isRegularFile(file) && FILE_EXTENSIONS.contains(extension);
      }
  }
