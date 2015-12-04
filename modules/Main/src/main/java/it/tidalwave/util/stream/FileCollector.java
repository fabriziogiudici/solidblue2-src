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
package it.tidalwave.util.stream;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collector;

/***********************************************************************************************************************
 *
 * An implementation of {@link Collector} for a {@link Stream&lt;String&gt;} that writes the strings to a file.
 * It can't be used with a parallel {@code Stream}, otherwise an {@link IllegalStateException} will be thrown. Even
 * in this case, the file could be partially written.
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: FileCollector.java,v bfe8bea5b104 2015/11/07 08:41:06 fabrizio $
 *
 **********************************************************************************************************************/
public final class FileCollector
  {
    /*******************************************************************************************************************
     * Creates a new instance.
     *
     * @param file          the file to write contents to
     * @param charset       the charset to use
     * @param openOptions   the options for opening the file
     * @return              nothing
     * @throws IOException  in case of I/O error
     *
     ******************************************************************************************************************/
    public static Collector<String, ?, ?> toFile (final Path file,
                                                  final Charset charset,
                                                  final OpenOption ... openOptions)
      throws IOException
      {
        final PrintWriter pw = new PrintWriter(Files.newBufferedWriter(file, charset, openOptions));
        final AtomicBoolean parallelChecker = new AtomicBoolean();

        return Collector.of(() -> safeGetPrintWriter(parallelChecker.getAndSet(true), pw),
                            PrintWriter::println,
                            (a, b) -> safeGetPrintWriter(true, pw), // never called
                            pw2 -> { pw2.close(); return null; });
      }

    private FileCollector()
      {
        throw new UnsupportedOperationException();
      }

    private static PrintWriter safeGetPrintWriter (final boolean condition, final PrintWriter pw)
      {
        if (condition)
          {
            pw.close();
            throw new IllegalStateException("Can't be used with a parallel Stream!");
          }

        return pw;
      }
  }
