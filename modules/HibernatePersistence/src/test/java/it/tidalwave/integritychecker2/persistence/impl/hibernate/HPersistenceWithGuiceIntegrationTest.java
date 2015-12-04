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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import it.tidalwave.integritychecker2.persistence.ImportController;
import it.tidalwave.integritychecker2.persistence.Persistence;
import it.tidalwave.integritychecker2.persistence.ScanRepository;
import it.tidalwave.integritychecker2.persistence.impl.PersistenceIntegrationTestSupport;
import java.sql.SQLException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
@Slf4j
public class HPersistenceWithGuiceIntegrationTest extends PersistenceIntegrationTestSupport
  {
    private Injector injector;

    private PersistService persistService ;

    @BeforeMethod
    public void prepare()
      throws SQLException
      {
        log.info("***** prepare()");
        final Properties properties = new Properties();
        properties.put("hibernate.connection.driver_class", "org.h2.Driver");
        properties.put("hibernate.connection.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        properties.put("hibernate.connection.pool_size", "1");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");

        injector = Guice.createInjector(new HModule(), new JpaPersistModule("SLBPU").properties(properties));
        persistService = injector.getInstance(PersistService.class);
        persistService.start();
        persistence = injector.getInstance(Persistence.class);
        persistence.createDataSource(); // FIXME: Hibernate is not using it
        persistence.createTables();
        scanRepository = injector.getInstance(ScanRepository.class);
        importController = injector.getInstance(ImportController.class);
      }

    @AfterMethod
    public void shutdown()
      {
        log.info("***** shutdown()");
        persistService.stop();
      }
  }
