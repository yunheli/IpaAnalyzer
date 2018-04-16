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

import com.ipa.analysis.TestBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author yh@fir.im
 */
public class CommonUtilsTest extends TestBase {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File tmpFolder;

    @Before
    public void init() {
        try {
            tmpFolder = temporaryFolder.newFolder("icon");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_process_success() throws IOException {

        InputStream input = new FileInputStream(getResourcePath("test.png").toString());

        byte[] bytes = CommonUtils.transIconToNormal(IOUtils.toByteArray(input));

        Files.write(Paths.get(tmpFolder.toString(), "icon.png"), bytes);

        System.out.println("finish");
    }
}
