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
 * $Id: JFXIntegrityCheckerPresentationAdapter.java,v a805d99df4b0 2015/11/03 19:51:11 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2.ui.impl.javafx;

import it.tidalwave.integritychecker2.ui.IntegrityCheckerFieldsBean;
import it.tidalwave.integritychecker2.ui.IntegrityCheckerPresentation;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici (Fabrizio.Giudici@tidalwave.it)
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class JFXIntegrityCheckerPresentation implements IntegrityCheckerPresentation
  {
    private final IntegrityCheckerPresentation adapter;

    private final Stage primaryStage;

    public JFXIntegrityCheckerPresentation (final Stage primaryStage)
      throws IOException
      {
        this.primaryStage = primaryStage;
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("JFXIntegrityCheckerPresentation.fxml"));
        loader.load();
        final Parent root = loader.getRoot();
        adapter = loader.getController();

        final Scene scene = new Scene(root, 600, 200);
        primaryStage.setTitle("SolidBlue II");
        primaryStage.setScene(scene);
        primaryStage.show();
      }

    @Override
    public void bind (final IntegrityCheckerFieldsBean fields)
      {
        adapter.bind(fields);
      }

    @Override
    public void dispose()
      {
        primaryStage.close();
      }
  }
