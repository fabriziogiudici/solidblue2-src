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

import it.tidalwave.integritychecker2.persistence.impl.PersistenceSupport;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class MBPersistence extends PersistenceSupport implements TransactionManager
  {
    private final SqlSessionFactory sqlSessionFactory;

    @Inject
    MBPersistence (final SqlSessionFactory sqlSessionFactory, final DataSource dataSource)
      {
        super.dataSource = dataSource;
        this.sqlSessionFactory = sqlSessionFactory;
      }

    @Override
    public <T> T runTransationally (final TransactionalTask<T> task)
      {
        try (final SqlSession session = sqlSessionFactory.openSession())
          {
            final T result = task.runInTransaction(session);
            session.commit(); // FIXME: bind transactional context to thread, reuse
            return result;
          }
        // FIXME: handle exceptions, perform rollback
      }
  }
