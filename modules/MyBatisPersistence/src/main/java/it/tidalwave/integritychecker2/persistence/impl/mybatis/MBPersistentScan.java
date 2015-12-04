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

import it.tidalwave.integritychecker2.model.Scan;
import it.tidalwave.integritychecker2.persistence.PersistentFileScan;
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import it.tidalwave.role.IdFactory;
import it.tidalwave.util.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import static lombok.AccessLevel.PACKAGE;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@AllArgsConstructor(access = PACKAGE)
@RequiredArgsConstructor(access = PACKAGE)
@EqualsAndHashCode(exclude = "transactionManager")
@ToString(exclude = "transactionManager")
public class MBPersistentScan implements PersistentScan
  {
    private TransactionManager transactionManager;

    private IdFactory idFactory;

    @Getter
    private final Id id;

    private final LocalDateTime creationDateTime;

    @Override
    public PersistentFileScan createFileScan (final String fileName, final String fingerprint)
      {
        final MBPersistentFileScan fileScan = new MBPersistentFileScan(transactionManager,
                                                                       this,
                                                                       idFactory.createId(PersistentFileScan.class),
                                                                       fileName,
                                                                       fingerprint);
        fileScan.insert();
        return fileScan;
      }

    void bind (final TransactionManager transactionManager, final IdFactory idFactory)
      {
        this.transactionManager = transactionManager;
        this.idFactory = idFactory;
      }

    void insert()
      {
        transactionManager.runTransationally(session ->
          {
            session.getMapper(MBPersistentScanMapper.class).insert(this);
            return null;
          });
      }

    @Override
    public Scan toModel()
      {
        final AtomicReference<Scan> holder = new AtomicReference<>(Scan.builder().creationDateTime(creationDateTime).build());
        findAllFileScans().stream().forEach(fs -> holder.getAndUpdate(scan -> scan.with(fs.toFileAndFingerprint())));
        return holder.get();
      }

    private List<MBPersistentFileScan> findAllFileScans()
      {
        return transactionManager.runTransationally(session ->
          {
            return session.getMapper(MBPersistentFileScanMapper.class).selectByScan(id.stringValue());
          });
      }
  }
