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

import com.google.inject.Inject;
import it.tidalwave.integritychecker2.persistence.PersistentScan;
import it.tidalwave.integritychecker2.persistence.ScanDao;
import it.tidalwave.role.IdFactory;
import java.time.LocalDateTime;
import java.util.List;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class MBScanDao implements ScanDao
  {
    private final TransactionManager transactionManager;

    private final IdFactory idFactory;

    @Inject
    MBScanDao (final TransactionManager transactionManager, final IdFactory idFactory)
      {
        this.transactionManager = transactionManager;
        this.idFactory = idFactory;
      }

    @Override
    public PersistentScan createScan (final LocalDateTime dateTime)
      {
        final MBPersistentScan scan = new MBPersistentScan(transactionManager,
                                                           idFactory,
                                                           idFactory.createId(PersistentScan.class),
                                                           dateTime);
        scan.insert();
        return scan;
      }

    @Override
    public List<PersistentScan> findAllScans()
      {
        return (List)transactionManager.runTransationally(session ->
          {
            final MBPersistentScanMapper mapper = session.getMapper(MBPersistentScanMapper.class);
            final List<MBPersistentScan> result = mapper.selectAll(); // FIXME

            for (final MBPersistentScan scan : result)
              {
                scan.bind(transactionManager, idFactory);
              }

            return result;
          });
      }
  }
