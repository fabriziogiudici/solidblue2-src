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
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import it.tidalwave.role.IdFactory;
import it.tidalwave.util.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class MBPersistentScan implements PersistentScan
  {
    private SqlSessionFactory sqlSessionFactory;

    private IdFactory idFactory;

    private Id id;

    private LocalDateTime creationDateTime;

    MBPersistentScan (final SqlSessionFactory sqlSessionFactory,
                      final IdFactory idFactory,
                      final Id id,
                      final LocalDateTime creationDateTime)
      {
        this.sqlSessionFactory = sqlSessionFactory;
        this.idFactory = idFactory;
        this.id = id;
        this.creationDateTime = creationDateTime;
      }

    public MBPersistentScan()
      {
      }

    @Override
    public PersistentFileScan createFileScan (final String fileName, final String fingerprint)
      {
        final MBPersistentFileScan fileScan = new MBPersistentFileScan(sqlSessionFactory,
                                                                       this,
                                                                       idFactory.createId(PersistentFileScan.class),
                                                                       fileName,
                                                                       fingerprint);
        fileScan.insert();
        return fileScan;
      }

    @Override
    public List<PersistentFileScan> findAllFileScans()
      {
        try (final SqlSession session = sqlSessionFactory.openSession())
          {
            final MBPersistentFileScanMapper mapper = session.getMapper(MBPersistentFileScanMapper.class);
            List<MBPersistentFileScan> result = mapper.selectByScan(id.stringValue());
            session.commit(); // FIXME
            return (List)result; // FIXME
          }
      }

    public String getId()
      {
        return id.stringValue();
      }

    public void setId (final String id)
      {
        this.id = new Id(id);
      }

    public Timestamp getCreationDateTime()
      {
        return Timestamp.valueOf(creationDateTime);
      }

    public void setCreationDateTime (final Timestamp creationDateTime)
      {
        this.creationDateTime = creationDateTime.toLocalDateTime();
      }

    @Override
    public int hashCode()
      {
        return Objects.hash(id, creationDateTime);
      }

    @Override
    public boolean equals (final Object obj)
      {
        if ((obj == null) || (getClass() != obj.getClass()))
          {
            return false;
          }

        final MBPersistentScan other = (MBPersistentScan)obj;

        return Objects.equals(this.id, other.id)
            && Objects.equals(this.creationDateTime, other.creationDateTime);
      }

    @Override
    public String toString()
      {
        return String.format("Scan(id: %s, creationDateTime: %s", id, creationDateTime);
      }
    
    void bind (final SqlSessionFactory sqlSessionFactory,  final IdFactory idFactory)
      {
        this.sqlSessionFactory = sqlSessionFactory;
        this.idFactory = idFactory;
      }

    void insert()
      {
        try (final SqlSession session = sqlSessionFactory.openSession())
          {
            final MBPersistentScanMapper mapper = session.getMapper(MBPersistentScanMapper.class);
            mapper.insert(this);
            session.commit(); // FIXME
          }
      }
  }
