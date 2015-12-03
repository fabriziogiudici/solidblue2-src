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

import it.tidalwave.integritychecker2.persistence.impl.mybatis.typehandler.IdTypeHandler;
import it.tidalwave.util.Id;
import java.util.List;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/***********************************************************************************************************************
 *
 * @author Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public interface MBPersistentFileScanMapper
  {
    @ConstructorArgs
      ({
        @Arg(column = "ID",          javaType = Id.class,     typeHandler = IdTypeHandler.class),
        @Arg(column = "SCAN_ID",     javaType = Id.class,     typeHandler = IdTypeHandler.class),
        @Arg(column = "FILE_NAME",   javaType = String.class),
        @Arg(column = "FINGERPRINT", javaType = String.class),
      })
    @Select("SELECT * FROM FILE_SCAN WHERE SCAN_ID=#{scanId}")
    public List<MBPersistentFileScan> selectByScan (@Param("scanId") String scanId);

    @Insert("INSERT INTO FILE_SCAN(ID, SCAN_ID, FILE_NAME, FINGERPRINT) VALUES ("
            + "#{id, typeHandler=it.tidalwave.integritychecker2.persistence.impl.mybatis.typehandler.IdTypeHandler}, "
            + "#{scanId, typeHandler=it.tidalwave.integritychecker2.persistence.impl.mybatis.typehandler.IdTypeHandler}, "
            + "#{fileName}, #{fingerprint})")
    void insert (MBPersistentFileScan fileScan);
  }
