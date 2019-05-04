/*
 * #%L
 * *********************************************************************************************************************
 *
 * SolidBlue - open source safe data
 * http://solidblue.tidalwave.it - hg clone https://bitbucket.org/tidalwave/solidblue2-src
 * %%
 * Copyright (C) 2015 - 2019 Tidalwave s.a.s. (http://tidalwave.it)
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
 * $Id: FileCollector.java,v bfe8bea5b104 2015/11/07 08:41:06 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.util;

import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public final class TimerTaskAdapterFactory
  {
    private static final Logger log = LoggerFactory.getLogger(TimerTaskAdapterFactory.class);

    @FunctionalInterface
    public static interface Task
      {
        public void run()
          throws Exception;
      }

    public static TimerTask toTimerTask (final Task task)
      {
        return new TimerTask()
          {
            @Override
            public void run()
              {
                try
                  {
                    task.run();
                  }
                catch (Exception e)
                  {
                    log.error("", e);
                  }
              }
          };
      }
  }
