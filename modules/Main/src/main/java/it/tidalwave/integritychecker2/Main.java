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
package it.tidalwave.integritychecker2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.nio.file.FileVisitOption.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Main.java,v b4f706516290 2015/11/07 08:47:17 fabrizio $
 *
 **********************************************************************************************************************/
public class Main
  {
    private static final List<String> FILE_EXTENSIONS = Arrays.asList("nef", "arw", "dng", "tif", "jpg");

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static void main (final String ... args)
      throws IOException
      {
        new Main().scan(Paths.get(args[0]));
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    private void scan (final Path targetPath)
      throws IOException
      {
        log.info("Scanning {}...", targetPath);
        final ProgressTracker progressTracker = new ProgressTracker();

        try (final Stream<Path> stream = Files.walk(targetPath, FOLLOW_LINKS);
             final FileStorage storage = new FileStorage(targetPath))
          {
            stream.filter(Main::matchesExtension)
                  .peek(progressTracker::notifyDiscoveredFile)
                  .collect(storage.getIntermediateCollector())
                  .stream()
                  .map(FileAndFingerprint::new)
                  .peek(progressTracker::notifyScannedFile)
                  .collect(storage.getFinalCollector());
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
