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
package it.tidalwave.integritychecker2.persistence;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Interface.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public interface PersistentScan
  {
    public PersistentFileScan createFileScan (String fileName, String fingerprint);

    default public PersistentFileScan importFileScanFromString (final String string)
      {
        final Pattern pattern = Pattern.compile("^MD5\\((.*)\\)=(.*)$");
        final Matcher matcher = pattern.matcher(string);

        if (!matcher.matches())
          {
            throw new IllegalArgumentException("No matches for " + string);
          }

        return createFileScan(matcher.group(1), matcher.group(2));
      }

    public List<PersistentFileScan> findAllFileScans();
  }
