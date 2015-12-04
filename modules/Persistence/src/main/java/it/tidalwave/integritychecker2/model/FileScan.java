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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.PACKAGE;
import lombok.EqualsAndHashCode;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
@Getter(PACKAGE)
@EqualsAndHashCode(exclude = "scan")
public class FileScan
  {
    private final Scan scan;

    private final String fileName;

    private final String fingerprint;

    public FileScan reparented (final Scan newScan)
      {
        return new FileScan(newScan, fileName, fingerprint);
      }

    public String toExportString()
      {
        return String.format("MD5(%s)=%s", fileName, fingerprint);
      }

    static FileScan fromImportString (final Scan scan, final String string)
      {
        final Pattern pattern = Pattern.compile("^MD5\\((.*)\\)=(.*)$");
        final Matcher matcher = pattern.matcher(string);

        if (!matcher.matches())
          {
            throw new IllegalArgumentException("No matches for " + string);
          }

        return new FileScan(scan, matcher.group(1), matcher.group(2));
      }
//
//    @Override
//    public int hashCode()
//      {
//        return Objects.hash(System.identityHashCode(scan), fileName, fingerprint);
//      }
//
//    @Override
//    public boolean equals (final Object object)
//      {
//        if (this == object)
//          {
//            return true;
//          }
//
//        if ((object == null) || (getClass() != object.getClass()))
//          {
//            return false;
//          }
//
//        final FileScan other = (FileScan)object;
//
//        return this.scan == other.scan // must have the same owner
//            && Objects.equals(this.fileName, other.fileName)
//            && Objects.equals(this.fingerprint, other.fingerprint);
//      }

    @Override
    public String toString()
      {
        return String.format("FileScan(scan=%d, fileName=%s, fingerprint=%s)",
                             System.identityHashCode(scan), fileName, fingerprint);
      }
  }
