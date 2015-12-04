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
 * $Id: JFXIntegrityCheckerPresentation.java,v a805d99df4b0 2015/11/03 19:51:11 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: JFXIntegrityCheckerPresentation.java,v a805d99df4b0 2015/11/03 19:51:11 fabrizio $
 *
 **********************************************************************************************************************/
public class JFXIntegrityCheckerPresentation implements IntegrityCheckerPresentation
  {
    @FXML
    private Label lbElapsedTime;

    @FXML
    private Label lbEstimatedRemainingTime;

    @FXML
    private Label lbTotalData;

    @FXML
    private Label lbProcessedData;

    @FXML
    private Label lbSpeed;

    @FXML
    private ProgressBar pbProgress;

    @FXML
    private Label lbProgress;

    @Override
    public void initialize()
      {
        populate(new IntegrityCheckerFieldsBean());
      }

    @Override
    public void renderBeginOfScan()
      {
        Platform.runLater(() -> pbProgress.setProgress(-1)); // indeterminate
      }

    @Override
    public void populate (final IntegrityCheckerFieldsBean fields)
      {
        Platform.runLater(() ->
          {
            lbTotalData.setText(fields.getTotal());
            lbProcessedData.setText(fields.getProcessed());
            lbElapsedTime.setText(fields.getElapsedTime());
            lbEstimatedRemainingTime.setText(fields.getRemainingTime());
            lbSpeed.setText(fields.getSpeed());
            final double progress = fields.getProgress();
            final boolean indeterminate = (progress == 0) && (pbProgress.getProgress() < 0);
            pbProgress.setProgress(indeterminate ? -1 : progress);
            lbProgress.setText(indeterminate || (progress == 0) ? "" : String.format("%.1f %%", 100 * progress));
          });
      }
  }