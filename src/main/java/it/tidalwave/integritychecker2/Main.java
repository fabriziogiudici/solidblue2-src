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
 * $Id: Main.java,v f48850f49198 2015/11/03 19:54:55 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.nio.channels.FileChannel.MapMode.*;
import static java.nio.file.FileVisitOption.*;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Main.java,v f48850f49198 2015/11/03 19:54:55 fabrizio $
 *
 **********************************************************************************************************************/
public class Main
  {
    private static final List<String> FILE_EXTENSIONS = Arrays.asList("nef", "arw", "dng", "tif", "jpg");

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private final static AtomicInteger discoveryCount = new AtomicInteger();
    private final static AtomicInteger scanCount = new AtomicInteger();
    private final static AtomicLong discoverySize = new AtomicLong();
    private final static AtomicLong scanSize = new AtomicLong();

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static void main (final String ... args)
      throws IOException
      {
        final Path targetPath = Paths.get(args[0]);
        log.info("Scanning {}...", targetPath);
        final Map<String, String> storage = Files.walk(targetPath, FOLLOW_LINKS)
                                                 .filter(Main::matchesExtension)
                                                 .peek(Main::notifyDiscoveredFile)
                                                 .collect(toList())
                                                 .stream()
                                                 .collect(toMap(p -> p.getFileName().toString(),
                                                                p -> computeFingerprint(p, "MD5"),
                                                                (v1, v2) -> v2));
        store(targetPath, storage);
      }

    /*******************************************************************************************************************
     *
     * Stores the collected data.
     *
     * @param   targetPath      the scanned directory
     * @param   storage         the data
     *
     ******************************************************************************************************************/
    private static void store (final Path targetPath, final Map<String, String> storage)
      throws IOException
      {
        final Path folder = targetPath.resolve(".it.tidalwave.solidblue2");
        final Path file = folder.resolve("fingerprints-j8.txt");
        Files.createDirectories(folder);
        log.info("Storing results into {} ...", file);

        try (final PrintWriter w = new PrintWriter(Files.newBufferedWriter(file, Charset.forName("UTF-8"))))
          {
            storage.entrySet().stream()
                              .sorted(comparing(Entry::getKey))
                              .forEach(e -> w.printf("MD5(%s)=%s\n", e.getKey(), e.getValue()));
          }
      }

    /*******************************************************************************************************************
     *
     * Updates the statistics for a freshly discovered file.
     *
     * @param   file            the file
     *
     ******************************************************************************************************************/
    private static void notifyDiscoveredFile (final Path file)
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
    private static void notifyScannedFile (final Path file)
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
    private static void logProgress()
      {
        final int sc = scanCount.get();
        final int dc = discoveryCount.get();
        final long ss = scanSize.get();
        final long ds = discoverySize.get();
        log.info("{}", String.format("Processed files: %d/%d (%d%%) - size: %dMB/%dMB (%d%%)",
                                     sc, dc, (100 * sc / dc),
                                     ss / 1_000_000, ds / 1_000_000, (100 * ss / ds)));
      }

    /*******************************************************************************************************************
     *
     * Computes the fingerprint of a file.
     *
     * @param   file            the file
     * @param   algorithm       the algorithm to use
     * @return                  the fingerprint
     *
     ******************************************************************************************************************/
    private static String computeFingerprint (final Path file, final String algorithm)
      {
        try
          {
            log.info("computeFingerprint({}, {})", file, algorithm);
            final MessageDigest digestComputer = MessageDigest.getInstance(algorithm);

            try (final RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r"))
              {
                final MappedByteBuffer byteBuffer = raf.getChannel().map(READ_ONLY, 0, Files.size(file));
                digestComputer.update(byteBuffer);
              }

            return toString(digestComputer.digest());
          }
        catch (NoSuchAlgorithmException | IOException e)
          {
            return e.getMessage();
          }
        finally
          {
            notifyScannedFile(file);
          }
      }

    /*******************************************************************************************************************
     *
     * Returns a hex representation of an array of bytes.
     *
     * @param   bytes           the bytes
     * @return                  the string
     *
     ******************************************************************************************************************/
    private static String toString (final byte[] bytes)
      {
        final StringBuilder builder = new StringBuilder();

        for (final byte b : bytes)
          {
            final int value = b & 0xff;
            builder.append(Integer.toHexString(value >>> 4)).append(Integer.toHexString(value & 0x0f));
          }

        return builder.toString();
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
