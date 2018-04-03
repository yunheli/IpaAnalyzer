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

package com.ipa.analysis;

import com.ipa.analysis.test.IpaParserImplTest;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yh@fir.im
 */
public class TestBase {

    protected Path getResourcePath(String resource) {
        return Paths.get(IpaParserImplTest.class.getClassLoader().getResource(resource).getPath());
    }
}
