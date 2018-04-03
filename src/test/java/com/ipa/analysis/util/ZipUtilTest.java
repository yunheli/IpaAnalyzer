package com.ipa.analysis.util;

import com.ipa.analysis.TestBase;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yh@fir.im
 */
public class ZipUtilTest extends TestBase {

    @Test
    public void should_test_get_strings_success() {
        List<byte[]> inputStreams = IpaUtil
            .fetchStrings(getResourcePath("test.ipa"));

        Assert.assertNotEquals(0, inputStreams.size());
    }

    @Test
    public void should_test_get_plist_success() {

        byte[] inputStream = IpaUtil
            .fetchPlist(getResourcePath("test.ipa"));

        Assert.assertNotNull(inputStream);
    }
}
