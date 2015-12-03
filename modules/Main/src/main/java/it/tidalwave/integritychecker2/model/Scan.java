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
package it.tidalwave.integritychecker2.model;

import it.tidalwave.integritychecker2.FileAndFingerprint;
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import it.tidalwave.integritychecker2.persistence.ScanRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import static java.util.stream.Collectors.toList;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@EqualsAndHashCode
@ToString
public class Scan
  {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final LocalDateTime creationDateTime;

    private final List<FileScan> fileScans;

    public Scan (final LocalDateTime creationDateTime)
      {
        this.creationDateTime = creationDateTime;
        this.fileScans = new ArrayList<>();
      }

    private Scan (final Scan oldScan, final Function<Scan, FileScan> scanFactory)
      {
        this.creationDateTime = oldScan.creationDateTime;
        this.fileScans = Stream.concat(oldScan.fileScans.stream().map(fs -> fs.reparented(this)),
                                       Stream.of(scanFactory.apply(this)))
                               .collect(toList());
      }

    public Scan withImportedFileScan (final String newString)
      {
        return new Scan(this, scan -> FileScan.fromImportString(scan, newString));
      }

    public Scan with (final FileAndFingerprint faf)
      {
        return new Scan(this, scan -> new FileScan(scan, faf.getFile().getFileName().toString(),
                                                         faf.getFingerPrint()));
      }

    public static List<Scan> loadAll (final ScanRepository repository)
      {
        return repository.findAllScans().stream().map(PersistentScan::toModel).collect(toList());
      }

    public void store (final ScanRepository repository)
      {
        repository.runInTransaction(() ->
          {
            final PersistentScan persistentScan = repository.createScan(creationDateTime);
            fileScans.stream().forEach(fs -> persistentScan.createFileScan(fs.getFileName(), fs.getFingerprint()));
          });
      }

    public void export (final Path file)
      throws IOException
      {
        final Stream<String> s = fileScans.stream().map(FileScan::toExportString);
        Files.write(file, (Iterable<String>)s::iterator, UTF_8);
      }
  }
