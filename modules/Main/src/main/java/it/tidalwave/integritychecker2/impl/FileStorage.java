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
 * $Id: FileCollector.java,v bfe8bea5b104 2015/11/07 08:41:06 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2.impl;

import it.tidalwave.integritychecker2.FileAndFingerprint;
import it.tidalwave.integritychecker2.Storage;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static it.tidalwave.util.TimerTaskAdapterFactory.toTimerTask;
import static it.tidalwave.util.stream.FileCollector.toFile;
import static java.util.Comparator.comparing;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class FileStorage implements Storage
  {
    private static final Logger log = LoggerFactory.getLogger(FileStorage.class);

    private static final int STORE_INTERVAL = 1000;

    private final Path storageFile;

    private final Map<Path, String> map = new ConcurrentHashMap<>();

    private final Timer timer = new Timer();

    /*******************************************************************************************************************
     *
     * Creates an instance whose backing file is inside the given folder.
     *
     * @param   folder          the target folder
     * @throws  IOException     in case of exception
     *
     ******************************************************************************************************************/
    public FileStorage (final Path folder)
      throws IOException
      {
        final Path storageFolder = Files.createDirectories(folder.resolve(".it.tidalwave.solidblue2"));
        storageFile = storageFolder.resolve("fingerprints-j8.txt");
        log.info("Storing results into {} ...", storageFile);
        timer.scheduleAtFixedRate(toTimerTask(this::storeToDisk), STORE_INTERVAL, STORE_INTERVAL);
      }

    /*******************************************************************************************************************
     *
     * Returns the intermediate {@link Collector} which stores placeholder entries for all the files.
     *
     * @return  the {@code Collector}
     *
     ******************************************************************************************************************/
    @Override
    public Collector<Path, ?, FileStorage> getIntermediateCollector()
      {
        return Collector.of(() -> this,
                            FileStorage::storeItem,
                            (a, b) -> a);
      }

    /*******************************************************************************************************************
     *
     * Returns the final {@link Collector} which stores final data.
     *
     * @return  the {@code Collector}
     *
     ******************************************************************************************************************/
    @Override
    public Collector<FileAndFingerprint, ?, FileStorage> getFinalCollector()
      {
        return Collector.of(() -> this,
                            FileStorage::storeItem,
                            (a, b) -> a);
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public Stream<Path> stream()
      {
        return map.keySet().stream();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public Stream<Path> parallelStream()
      {
        return map.keySet().parallelStream();
      }

    /*******************************************************************************************************************
     *
     * {@inheritDoc}
     *
     ******************************************************************************************************************/
    @Override
    public void close()
      throws IOException
      {
        log.info("close()");
        timer.cancel();
        storeToDisk();
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private void storeItem (final Path file)
      {
        map.put(file, "unavailable");
      }

    /*******************************************************************************************************************
     *
     *
     ******************************************************************************************************************/
    private void storeItem (final FileAndFingerprint faf)
      {
        map.put(faf.getFile(), faf.getFingerPrint());
      }

    /*******************************************************************************************************************
     *
     * Stores the collected data.
     *
     ******************************************************************************************************************/
    private void storeToDisk()
      throws IOException
      {
        map.entrySet().stream()
                      .sorted(comparing(Map.Entry::getKey))
                      .map(e -> String.format("MD5(%s)=%s", e.getKey().getFileName().toString(), e.getValue()))
                      .collect(toFile(storageFile, Charset.forName("UTF-8")));
      }
  }
