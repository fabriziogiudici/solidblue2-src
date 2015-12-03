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
 * $Id: FileCollector.java,v bfe8bea5b104 2015/11/07 08:41:06 fabrizio $
 *
 * *********************************************************************************************************************
 * #L%
 */
package it.tidalwave.integritychecker2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici <Fabrizio dot Giudici at tidalwave dot it>
 * @version $Id: Class.java,v 631568052e17 2013/02/19 15:45:02 fabrizio $
 *
 **********************************************************************************************************************/
public class FileAndFingerprint
  {
    private static final Logger log = LoggerFactory.getLogger(FileAndFingerprint.class);

    private final Path file;

    private final String fingerPrint;

    public FileAndFingerprint (final Path file)
      {
        this.file = file;
        this.fingerPrint = computeFingerprint("MD5");
      }

    public FileAndFingerprint (final Path file, final String fingerPrint)
      {
        this.file = file;
        this.fingerPrint = fingerPrint;
      }

    public Path getFile()
      {
        return file;
      }

    public String getFingerPrint()
      {
        return fingerPrint;
      }

    @Override
    public String toString()
      {
        return String.format("FileAndFingerprint(path=%s, fingerPrint=%s)", file.getFileName().toString(), fingerPrint);
      }

    /*******************************************************************************************************************
     *
     * Computes the fingerprint of a file.
     *
     * @param   file            the file
     * @param   algorithm       the algorithm to use
     * @return                  the fingerprint
     *
     ******************************************************************************************************************/
    private String computeFingerprint (final String algorithm)
      {
        try
          {
            log.info("computeFingerprint({}, {})", file, algorithm);
            final MessageDigest digestComputer = MessageDigest.getInstance(algorithm);

            try (final RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r"))
              {
                final MappedByteBuffer byteBuffer = raf.getChannel().map(READ_ONLY, 0, Files.size(file));
                digestComputer.update(byteBuffer);
              }

            return toString(digestComputer.digest());
          }
        catch (NoSuchAlgorithmException | IOException e)
          {
            return e.getMessage();
          }
      }

    /*******************************************************************************************************************
     *
     * Returns a hex representation of an array of bytes.
     *
     * @param   bytes           the bytes
     * @return                  the string
     *
     ******************************************************************************************************************/
    private static String toString (final byte[] bytes)
      {
        final StringBuilder builder = new StringBuilder();

        for (final byte b : bytes)
          {
            final int value = b & 0xff;
            builder.append(Integer.toHexString(value >>> 4)).append(Integer.toHexString(value & 0x0f));
          }

        return builder.toString();
      }
  }