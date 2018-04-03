package com.ipa.analysis.test;

import com.ipa.analysis.IpaParser;
import com.ipa.analysis.IpaParserImpl;
import com.ipa.analysis.TestBase;
import com.ipa.analysis.domain.IpaInfo;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author yh@fir.im
 */
public class IpaParserImplTest extends TestBase {

    @Test
    public void parse() throws Exception {
        IpaParser ipaParser = new IpaParserImpl(getResourcePath("test.ipa"));
        IpaInfo parse = ipaParser.parse();

        Files.write(Paths.get("/tmp/icon.png"), parse.getIcon());
        Assert.assertNotNull(parse);
    }

    @Test
    public void parseStrings() throws Exception {
        IpaParser ipaParser = new IpaParserImpl(getResourcePath("test.ipa"));
        List<String> strings = ipaParser.parseStrings();
        Assert.assertNotNull(strings);
    }
}
