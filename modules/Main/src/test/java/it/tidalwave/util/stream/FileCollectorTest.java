/*
 * #%L
 * *********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * http://solidblue.tidalwave.it - hg clone https://bitbucket.org/tidalwave/solidblue2-src
 * %%
 * Copyright (C) 2015 - 2019 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id: FileCollectorTest.java,v ef3854f8082e 2015/11/07 08:38:53 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util.stream;

import it.tidalwave.util.test.FileComparisonUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: FileCollectorTest.java,v ef3854f8082e 2015/11/07 08:38:53 fabrizio $
 *
 **********************************************************************************************************************/
public class FileCollectorTest
  {
    private static final String ACTUAL_FILE = "target/actual-file.txt";
    private static final String EXPECTED_FILE = "target/test-classes/expected-file.txt";

    private Collector<String, ?, ?> underTest;

    private Path file;

    @BeforeMethod
    public void setup()
      throws IOException
      {
        file = Paths.get(ACTUAL_FILE);
        underTest = FileCollector.toFile(file, Charset.forName("UTF-8"));
      }

    @Test(invocationCount = 100)
    public void must_produce_a_correct_file()
      throws IOException
      {
        IntStream.range(0, 10000)
                 .mapToObj(n -> "String #" + n)
                 .collect(underTest);

        FileComparisonUtils.assertSameContents(Paths.get(EXPECTED_FILE).toFile(), file.toFile());
      }

    @Test(invocationCount = 100,
          expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = ".*Can't be used with a parallel Stream!")
    public void must_fail_with_a_parallel_stream()
      {
        IntStream.range(0, 10000)
                 .mapToObj(n -> "String #" + n)
                 .collect(toList())
                 .parallelStream()
                 .collect(underTest);
      }
  }
