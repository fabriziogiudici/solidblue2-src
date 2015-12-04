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

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: IntegrityCheckerFieldsBean.java,v a805d99df4b0 2015/11/03 19:51:11 fabrizio $
 *
 **********************************************************************************************************************/
public class IntegrityCheckerFieldsBean
  {
    private String elapsedTime = " ";

    private String remainingTime = " ";

    private String total = " ";

    private String processed = " ";

    private String speed = " ";

    private String pending = " ";

    private String running = " ";

    private float progress = 0;

    private String progressLabel = " ";

    public String getElapsedTime()
      {
        return elapsedTime;
      }

    public void setElapsedTime (String elapsedTime)
      {
        this.elapsedTime = elapsedTime;
      }

    public String getRemainingTime()
      {
        return remainingTime;
      }

    public void setRemainingTime (String remainingTime)
      {
        this.remainingTime = remainingTime;
      }

    public String getTotal()
      {
        return total;
      }

    public void setTotal (String total)
      {
        this.total = total;
      }

    public String getProcessed()
      {
        return processed;
      }

    public void setProcessed (String processed)
      {
        this.processed = processed;
      }

    public String getSpeed()
      {
        return speed;
      }

    public void setSpeed (String speed)
      {
        this.speed = speed;
      }

    public String getPending()
      {
        return pending;
      }

    public void setPending (String pending)
      {
        this.pending = pending;
      }

    public String getRunning()
      {
        return running;
      }

    public void setRunning (String running)
      {
        this.running = running;
      }

    public float getProgress()
      {
        return progress;
      }

    public void setProgress (float progress)
      {
        this.progress = progress;
      }

    public String getProgressLabel()
      {
        return progressLabel;
      }

    public void setProgressLabel (String progressLabel)
      {
        this.progressLabel = progressLabel;
      }
  }
