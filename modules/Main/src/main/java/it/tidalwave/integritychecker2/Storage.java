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
package it.tidalwave.integritychecker2;

import java.nio.file.Path;
import java.util.stream.Collector;
import java.util.stream.Stream;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public interface Storage extends AutoCloseable
  {
    /*******************************************************************************************************************
     *
     * Returns the intermediate {@link Collector} which stores placeholder entries for all the files.
     *
     * @return  the {@code Collector}
     *
     ******************************************************************************************************************/
    public Collector<Path, ?, ? extends Storage> getIntermediateCollector();

    /*******************************************************************************************************************
     *
     * Returns the final {@link Collector} which stores final data.
     *
     * @return  the {@code Collector}
     *
     ******************************************************************************************************************/
    public Collector<FileAndFingerprint, ?, ? extends Storage> getFinalCollector();

    /*******************************************************************************************************************
     *
     * Returns a {@link Stream} of the {@link Path}s previously collected by the intermediate [@link Collector}.
     *
     * @see     #getIntermediateCollector()
     * @return  the {@code Stream}
     *
     ******************************************************************************************************************/
    public Stream<Path> stream();

    /*******************************************************************************************************************
     *
     * Returns a parallel {@link Stream} of the {@link Path}s previously collected by the intermediate [@link Collector}.
     *
     * @see     #getIntermediateCollector()
     * @return  the {@code Stream}
     *
     ******************************************************************************************************************/
    public Stream<Path> parallelStream();
  }
