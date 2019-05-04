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
 * $Id: Main.java,v b4f706516290 2015/11/07 08:47:17 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2;

import it.tidalwave.integritychecker2.impl.DefaultScanController;
import it.tidalwave.integritychecker2.ui.IntegrityCheckerPresentation;
import it.tidalwave.integritychecker2.ui.impl.javafx.JFXIntegrityCheckerPresentation;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.stage.Stage;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Main.java,v b4f706516290 2015/11/07 08:47:17 fabrizio $
 *
 **********************************************************************************************************************/
public class Main extends Application
  {
    private static Path targetPath;

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    public static void main (final String ... args)
      throws IOException
      {
        targetPath = Paths.get(args[0]);
        launch(args);
      }

    /*******************************************************************************************************************
     *
     *
     *
     ******************************************************************************************************************/
    @Override
    public void start (final Stage primaryStage)
      throws IOException
      {
        final IntegrityCheckerPresentation presentation = new JFXIntegrityCheckerPresentation(primaryStage);
        final ScanController scanController = new DefaultScanController(presentation);
        Executors.newSingleThreadExecutor().execute(() -> scanController.scan(targetPath));
      }
  }
