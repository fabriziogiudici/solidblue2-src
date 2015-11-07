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
 * $Id: FileCollector.java,v ef3854f8082e 2015/11/07 08:38:53 fabrizio $
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
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/***********************************************************************************************************************
 *
 * An implementation of {@link Collector} for a {@link Stream&lt;String&gt;} that writes the strings to a file.
 * It can't be used with a parallel {@code Stream}, otherwise an {@link IllegalStateException} will be thrown. Even
 * in this case, the file could be partially written.
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: FileCollector.java,v ef3854f8082e 2015/11/07 08:38:53 fabrizio $
 *
 **********************************************************************************************************************/
public final class FileCollector implements Collector<String, PrintWriter, Void>
  {
    private final PrintWriter pw;

    private final AtomicBoolean parallelChecker = new AtomicBoolean();

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
        return new FileCollector(file, charset, openOptions);
      }

    private FileCollector()
      {
        throw new UnsupportedOperationException();
      }

    private FileCollector (final Path file, final Charset charset, final OpenOption ... openOptions)
      throws IOException
      {
        pw = new PrintWriter(Files.newBufferedWriter(file, charset, openOptions));
      }

    @Override
    public Supplier<PrintWriter> supplier()
      {
        return this::oneShotPrintWriterSupplier;
      }

    @Override
    public BiConsumer<PrintWriter, String> accumulator()
      {
        return PrintWriter::println;
      }

    @Override
    public BinaryOperator<PrintWriter> combiner()
      {
        return (a, b) -> { fail(); return null; }; // never called
      }

    @Override
    public Function<PrintWriter, Void> finisher()
      {
        return pw -> { pw.close(); return null; };
      }

    @Override
    public Set<Characteristics> characteristics()
      {
        return Collections.emptySet();
      }

    private PrintWriter oneShotPrintWriterSupplier()
      {
        if (parallelChecker.getAndSet(true))
          {
            fail();
          }

        return pw;
      }

    private void fail()
      {
        pw.close();
        throw new IllegalStateException("Can't be used with a parallel Stream!");
      }
  }
