/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ipa.analysis.util;

import static org.apache.commons.io.IOUtils.toByteArray;

import com.ipa.analysis.exception.AnalysisException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * @author yh@fir.im
 */
public class CommonUtils {

    private final static Logger LOGGER = Logger.getLogger(CommonUtils.class.toString());

    private final static String PNG_DEFRY = "pngdefry";

    private final static String TMP_FOLDER = "/tmp/ipa_analysis";

    private final static String ICON_SUFFIX = ".png";

    private final static String NORMAL_SUFFIX = "oaplvid";


    /**
     * trans ipa icon to normal
     * @param input
     */
    public static byte[] transIconToNormal(byte[] input) throws AnalysisException {

        String[] commands;

        // create temp folder to operate
        createTmpFolders();

        Path origin = dumpToFile(input);
        assertNull(origin, "origin is null");

        Path dest = Paths.get(origin.getParent().toString(),
            origin.toFile().getName().split(ICON_SUFFIX)[0] + NORMAL_SUFFIX + ICON_SUFFIX);
        assertNull(dest, "dest is null");

        Path pngdefry = saveExecutedJarToTmp(CommonUtils.class.getClassLoader().getResourceAsStream(PNG_DEFRY));
        assertNull(pngdefry, "pngdefry is null");

        // set file to can executable
        pngdefry.toFile().setExecutable(true);

        commands = new String[]{
            pngdefry.toString(),
            "-s" + NORMAL_SUFFIX,
            "-o" + dest.getParent(),
            origin.toString()
        };
        String cmd = String.join(" ", commands);

        execute(cmd);

        try {
            return toByteArray(new FileInputStream(dest.toFile()));
        } catch (IOException e) {
            throw new AnalysisException(" Trans to normal icon exception " + e.getMessage());
        } finally {
            deleteFiles(origin);
            deleteFiles(dest);
        }
    }

    private static void execute(String cmd) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process ps = rt.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                LOGGER.info(line);
            }

        } catch (Throwable e) {
            LOGGER.severe(" Trans image format exception " + e.getMessage());
            throw new AnalysisException("execute cmd exception " + e.getMessage());
        }
    }

    private static void assertNull(Object flag, String message) {
        if (Objects.isNull(flag)) {
            throw new AnalysisException(message + "is null");
        }
    }

    private static Path saveExecutedJarToTmp(InputStream inputStream) {
        Path dest = Paths.get(TMP_FOLDER.toString(), PNG_DEFRY);

        try {
            Files.write(dest, IOUtils.toByteArray(inputStream));
        } catch (IOException e) {
            return null;
        }
        return dest;
    }

    private static void createTmpFolders() {
        if (!Paths.get(TMP_FOLDER).toFile().exists()) {
            try {
                Files.createDirectories(Paths.get(TMP_FOLDER));
            } catch (IOException e) {
                LOGGER.severe("Create analysis tmp folder exception " + e.getMessage());
            }
        }
    }

    private static Path dumpToFile(byte[] input) {
        Path dest = Paths.get(TMP_FOLDER, UUID.randomUUID().toString() + ICON_SUFFIX);
        try {
            Files.write(dest, input);
        } catch (IOException e) {
            return null;
        }
        return dest;
    }

    private static void deleteFiles(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
        }
    }
}
