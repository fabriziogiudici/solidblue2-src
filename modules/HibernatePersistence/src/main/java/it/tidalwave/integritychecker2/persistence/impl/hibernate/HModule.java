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

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import it.tidalwave.integritychecker2.persistence.ImportController;
import it.tidalwave.integritychecker2.persistence.Persistence;
import it.tidalwave.integritychecker2.persistence.ScanRepository;
import it.tidalwave.integritychecker2.persistence.impl.DefaultIdFactory;
import it.tidalwave.integritychecker2.persistence.impl.DefaultImportController;
import it.tidalwave.integritychecker2.persistence.impl.DefaultPersistence;
import it.tidalwave.role.IdFactory;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class HModule extends AbstractModule
  {
    private final Properties properties = new Properties();

    private final JdbcDataSource dataSource = new JdbcDataSource(); // FIXME: use DBCP?

    public HModule()
      {
        properties.put("hibernate.connection.datasource", dataSource);
        properties.put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
      }

    public HModule properties (final Map<?, ?> properties)
      {
        this.properties.putAll(properties);
        return this;
      }

    @Override
    protected void configure()
      {
        dataSource.setURL(properties.getProperty("db.url"));
        install(new JpaPersistModule("SLBPU").properties(properties));
        bind(DataSource.class).toInstance(dataSource);
        bind(IdFactory.class).to(DefaultIdFactory.class);
        bind(ImportController.class).to(DefaultImportController.class);
        bind(Persistence.class).to(DefaultPersistence.class);
        bind(ScanRepository.class).to(HScanRepository.class);
      }
  }
