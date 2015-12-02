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
package it.tidalwave.integritychecker2.persistence.impl.hibernate;

import it.tidalwave.integritychecker2.persistence.PersistentFileScan;
import it.tidalwave.util.Id;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@Entity
@Table(name = "FILE_SCAN")
public class HPersistentFileScan implements PersistentFileScan, Serializable
  {
    private static final long serialVersionUID = -7576194500313591212L;

    @javax.persistence.Id
    @Column(name = "ID", length = 36)
    @Type(type="it.tidalwave.integritychecker2.persistence.impl.hibernate.IdUserType")
    private Id id;

    @Column(name = "FILE_NAME", length = 200)
    private String fileName;

    @Column(name = "FINGERPRINT", length = 32)
    private String fingerprint;

    @JoinColumn(name = "SCAN_ID")
    @ManyToOne
    private HPersistentScan scan;

    protected HPersistentFileScan()
      {
      }

    HPersistentFileScan (final Id id, final HPersistentScan scan, final String fileName, final String fingerprint)
      {
        this.id = id;
        this.scan = scan;
        this.fileName = fileName;
        this.fingerprint = fingerprint;
      }

    @Override
    public int hashCode()
      {
        return Objects.hash(id);
      }

    @Override
    public boolean equals (final Object obj)
      {
        if ((obj == null) || !(obj instanceof HPersistentFileScan))
          {
            return false;
          }

        final HPersistentFileScan other = (HPersistentFileScan)obj;
        return Objects.equals(this.id, other.id);
      }

    @Override
    public String toString()
      {
        return String.format("FileScan(id: %s, fileName: %s, fingerPrint: %s", id, fileName, fingerprint);
      }

    @Override
    public String toExportString()
      {
        return String.format("MD5(%s)=%s", fileName, fingerprint);
      }

    public String getFileName()
      {
        return fileName;
      }

    public void setFileName (String fileName)
      {
        this.fileName = fileName;
      }

    public String getFingerprint()
      {
        return fingerprint;
      }

    public void setFingerprint (String fingerprint)
      {
        this.fingerprint = fingerprint;
      }
  }
