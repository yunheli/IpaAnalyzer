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

import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;

/**
 * @author yh@fir.im
 */
public class IpaUtil {

    public static byte[] fetchPlist(Path path) {
        List<byte[]> inputStreams = doFetchInputStreamFromZip(path, "Info.plist");
        if (inputStreams.size() == 0) {
            return null;
        }

        return inputStreams.get(0);
    }


    /**
     * fetch ipa resource files
     * @param path
     * @return
     */
    public static List<byte[]> fetchStrings(Path path) {
        return doFetchInputStreamFromZip(path, ".*strings");
    }

    public static byte[] fetchThing(Path path, String name) {
        List<byte[]> inputStreams = doFetchInputStreamFromZip(path, name);
        if (inputStreams.size() == 0) {
            return null;
        }

        return inputStreams.get(0);
    }

    /**
     * fetch ipa resource according to filename(regex)
     * @param path
     * @param regex
     * @return
     */
    private static List<byte[]> doFetchInputStreamFromZip(Path path, String regex) {
        List<byte[]> inputStreams = new LinkedList<>();
        try (ZipFile zipFile = new ZipFile(path.toString())) {

            for (Enumeration e = zipFile.entries(); e.hasMoreElements(); ) {
                ZipEntry zipEntry = (ZipEntry) e.nextElement();

                if (Pattern.matches(regex, processName(zipEntry.getName()))) {
                    inputStreams.add(IOUtils.toByteArray(zipFile.getInputStream(zipEntry)));
                }
            }
        } catch (Throwable throwable) {
            System.out.println(" Fetch from zip exception - " + throwable.getMessage());
        }

        return inputStreams;
    }


    /**
     * process file names
     * @param name
     * @return
     */
    private static String processName(String name) {
        String[] split = name.split("/");

        if (split.length < 3) {
            return name;
        }

        split[0] = "";
        split[1] = "";
        String join = String.join("/", split);
        return join.substring(2, join.length());
    }
}
