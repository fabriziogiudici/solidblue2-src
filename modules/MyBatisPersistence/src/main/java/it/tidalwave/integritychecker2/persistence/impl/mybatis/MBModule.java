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

import com.google.inject.name.Names;
import it.tidalwave.integritychecker2.persistence.ImportController;
import it.tidalwave.integritychecker2.persistence.Persistence;
import it.tidalwave.integritychecker2.persistence.impl.DefaultIdFactory;
import it.tidalwave.role.IdFactory;
import java.util.Properties;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import it.tidalwave.integritychecker2.persistence.ScanRepository;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class MBModule extends MyBatisModule
  {
    @Override
    protected void initialize()
      {
        install(JdbcHelper.H2_IN_MEMORY_NAMED);
        final Properties properties = new Properties();
        properties.setProperty("mybatis.environment.id", "test");
        properties.setProperty("JDBC.schema", "test;DB_CLOSE_DELAY=-1");

        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        Names.bindProperties(binder(), properties);
        addMapperClass(MBPersistentScanMapper.class);
        addMapperClass(MBPersistentFileScanMapper.class);

        bind(Persistence.class).to(MBPersistence.class);
        bind(TransactionManager.class).to(MBPersistence.class);
        bind(IdFactory.class).to(DefaultIdFactory.class);
        bind(ScanRepository.class).to(MBScanRepository.class);
        bind(ImportController.class).to(MBImportController.class);
      }
  }
