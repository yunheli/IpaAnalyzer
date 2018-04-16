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

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import com.ipa.analysis.domain.IpaInfo;
import com.ipa.analysis.util.CommonUtils;
import com.ipa.analysis.util.IpaUtil;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author yh@fir.im
 */
public class IpaParserImpl implements IpaParser {

    private Path path;

    private NSDictionary plist;

    public IpaParserImpl(Path path) {
        this.path = path;
    }

    @Override
    public IpaInfo parse() {
        byte[] inputStream = IpaUtil.fetchPlist(path);
        IpaInfo ipaInfo = null;
        try {
            this.plist = (NSDictionary) PropertyListParser.parse(inputStream);
            ipaInfo = transToInfo();
            ipaInfo.setIcon(resolveIconFromPlist());
        } catch (Throwable e) {
            System.out.println("Parse Plist Exception - " + e.getMessage());
        }
        return ipaInfo;
    }

    @Override
    public List<String> parseStrings() {

        List<String> stringList = new LinkedList<>();
        List<byte[]> files = IpaUtil.fetchStrings(path);
        try {
            for (byte[] file : files) {
                NSDictionary nsObject = (NSDictionary) PropertyListParser.parse(file);
                stringList.addAll(transToStrings(nsObject));
            }
        } catch (Throwable throwable) {
        }

        return stringList;
    }

    private List<String> transToStrings(NSDictionary nsDictionary) {

        List<String> strings = new LinkedList<>();
        for (NSObject nsObject : nsDictionary.values()) {
            strings.add(nsObject.toString());
        }
        return strings;
    }

    private IpaInfo transToInfo() {
        return new IpaInfo(
            getValueFromPlist("CFBundleDisplayName"),
            getValueFromPlist("CFBundleIdentifier"),
            getValueFromPlist("CFBundleVersion"),
            getValueFromPlist("CFBundleShortVersionString"));
    }

    private String getValueFromPlist(String key) {
        return plist.get(key).toString();
    }

    private byte[] resolveIconFromPlist() {
        NSDictionary cfBundleIcons = (NSDictionary) plist.get("CFBundleIcons");

        if (Objects.isNull(cfBundleIcons)) {
            return null;
        }
        NSDictionary cfBundlePrimaryIcon = (NSDictionary) cfBundleIcons.get("CFBundlePrimaryIcon");

        if (Objects.isNull(cfBundlePrimaryIcon)) {
            return null;
        }

        NSArray nsArray = (NSArray) cfBundlePrimaryIcon.get("CFBundleIconFiles");
        if (Objects.isNull(nsArray)) {
            return null;
        }

        NSObject[] array = nsArray.getArray();
        byte[] inputStream = null;
        if (array.length != 0) {
            for (NSObject nsObject : array) {
                byte[] bytes = IpaUtil.fetchThing(path, ".*" + nsObject + ".*");

                if (Objects.isNull(inputStream)) {
                    inputStream = bytes;
                }

                if (!Objects.isNull(inputStream) && !Objects.isNull(bytes)) {

                    if (bytes.length > inputStream.length) {
                        inputStream = bytes;
                    }
                }
            }

            // tran icon to normal show
            try {
                inputStream = CommonUtils.transIconToNormal(inputStream);
            } catch (Throwable e) {
            }
        }
        return inputStream;
    }
}
