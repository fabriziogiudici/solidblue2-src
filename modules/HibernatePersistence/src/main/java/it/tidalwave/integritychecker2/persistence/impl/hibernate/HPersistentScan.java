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
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import it.tidalwave.util.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Type;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@Entity
@Access(FIELD)
public class HPersistentScan implements PersistentScan
  {
    @javax.persistence.Id
    @Type(type="it.tidalwave.integritychecker2.persistence.impl.hibernate.IdUserType")
    private Id id;

    @Type(type="it.tidalwave.integritychecker2.persistence.impl.hibernate.LocalDateTimeUserType")
    private LocalDateTime creationDateTime;

    @OneToMany(fetch = EAGER, cascade = PERSIST)
    private List<HPersistentFileScan> fileScans;

    HPersistentScan()
      {
      }

    HPersistentScan (final Id id, final LocalDateTime creationDateTime)
      {
        this.id = id;
        this.creationDateTime = creationDateTime;
      }

    @Override
    public int hashCode()
      {
        return Objects.hash(id, creationDateTime);
      }

    @Override
    public boolean equals (final Object obj)
      {
        if ((obj == null) || !(obj instanceof HPersistentScan))
          {
            return false;
          }

        final HPersistentScan other = (HPersistentScan)obj;
        return Objects.equals(this.id, other.id);
      }

    @Override
    public String toString()
      {
        return String.format("Scan(id: %s, creationDateTime: %s", id, creationDateTime);
      }

    @Override
    public PersistentFileScan createFileScan (String fileName, String fingerprint)
      {
        final Id i = new Id(UUID.randomUUID().toString()); // FIXME: use IdFactory
        final HPersistentFileScan fileScan = new HPersistentFileScan(i, this, fileName, fingerprint);

        if (fileScans == null)
          {
            fileScans = new ArrayList<>();
          }

        fileScans.add(fileScan);

        return fileScan;
      }

    @Override
    public List<PersistentFileScan> findAllFileScans()
      {
        return (List)fileScans;
      }
  }
