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
package it.tidalwave.integritychecker2.persistence.impl.mybatis;

import it.tidalwave.integritychecker2.persistence.PersistentFileScan;
import it.tidalwave.util.Id;
import java.util.Objects;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class MBPersistentFileScan implements PersistentFileScan
  {
    private SqlSessionFactory sqlSessionFactory;

//    private final MBPersistentScan scan; FIXME

    private Id scanId;

    private Id id;

    private String fileName;

    private String fingerprint;

    MBPersistentFileScan (final SqlSessionFactory sqlSessionFactory,
                          final MBPersistentScan scan,
                          final Id id,
                          final String fileName,
                          final String fingerprint)
      {
        this.sqlSessionFactory = sqlSessionFactory;
        this.scanId = new Id(scan.getId()); // FIXME
        this.id = id;
        this.fileName = fileName;
        this.fingerprint = fingerprint;
      }

    public MBPersistentFileScan()
      {
      }

    @Override
    public String toExportString()
      {
        return String.format("MD5(%s)=%s", fileName, fingerprint);
      }

    public String getId()
      {
        return id.stringValue();
      }

    public void setId (final String id)
      {
        this.id = new Id(id);
      }

    public String getScanId()
      {
        return scanId.stringValue();
      }

    public void setScanId (final String scanId)
      {
        this.scanId = new Id(scanId);
      }

    public String getFileName()
      {
        return fileName;
      }

    public void setFileName (final String fileName)
      {
        this.fileName = fileName;
      }

    public String getFingerprint()
      {
        return fingerprint;
      }

    public void setFingerprint (final String fingerprint)
      {
        this.fingerprint = fingerprint;
      }

    @Override
    public int hashCode()
      {
        return Objects.hash(id, scanId, fileName, fingerprint);
      }

    @Override
    public boolean equals (final Object obj)
      {
        if ((obj == null) || (getClass() != obj.getClass()))
          {
            return false;
          }

        final MBPersistentFileScan other = (MBPersistentFileScan)obj;

        return Objects.equals(this.scanId, other.scanId)
            && Objects.equals(this.id, other.id)
            && Objects.equals(this.fileName, other.fileName)
            && Objects.equals(this.fingerprint, other.fingerprint);
      }

    @Override
    public String toString()
      {
        return String.format("FileScan(id: %s, fileName: %s, fingerPrint: %s", id, fileName, fingerprint);
      }

    void insert()
      {
        try (final SqlSession session = sqlSessionFactory.openSession())
          {
            final MBPersistentFileScanMapper mapper = session.getMapper(MBPersistentFileScanMapper.class);
            mapper.insert(this);
            session.commit(); // FIXME
          }
      }
  }
