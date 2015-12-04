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
 * $Id: IntegrityCheckerFieldsBean.java,v a805d99df4b0 2015/11/03 19:51:11 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2.ui;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: IntegrityCheckerFieldsBean.java,v a805d99df4b0 2015/11/03 19:51:11 fabrizio $
 *
 **********************************************************************************************************************/

public class IntegrityCheckerFieldsBean
  {
    private final StringProperty elapsedTime = new SimpleStringProperty(" ");

    private final StringProperty remainingTime = new SimpleStringProperty(" ");

    private final StringProperty total = new SimpleStringProperty(" ");

    private final StringProperty processed = new SimpleStringProperty(" ");

    private final StringProperty speed = new SimpleStringProperty(" ");

    private final StringProperty pending = new SimpleStringProperty(" ");

    private final StringProperty running = new SimpleStringProperty(" ");

    private final FloatProperty progress = new SimpleFloatProperty(-1);

    private final StringProperty progressLabel = new SimpleStringProperty(" ");

    public StringProperty elapsedTimeProperty()
      {
        return elapsedTime;
      }

    public StringProperty remainingTimeProperty()
      {
        return remainingTime;
      }

    public StringProperty totalProperty()
      {
        return total;
      }

    public StringProperty processedProperty()
      {
        return processed;
      }

    public StringProperty speedProperty()
      {
        return speed;
      }

    public StringProperty pendingProperty()
      {
        return pending;
      }

    public StringProperty runningProperty()
      {
        return running;
      }

    public FloatProperty progressProperty()
      {
        return progress;
      }

    public StringProperty progressLabelProperty()
      {
        return progressLabel;
      }
  }
