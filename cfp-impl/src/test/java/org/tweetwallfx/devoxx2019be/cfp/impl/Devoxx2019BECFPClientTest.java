/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 TweetWallFX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tweetwallfx.devoxx2019be.cfp.impl;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.tweetwallfx.config.Configuration;
import org.tweetwallfx.devoxx.cfp.test.CFPClientTestBase;

public class Devoxx2019BECFPClientTest extends CFPClientTestBase {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    static {
        checkCfpReachable(Configuration.getInstance()
                .getConfigTyped(
                        CFPClientSettings.CONFIG_KEY,
                        CFPClientSettings.class)
                .getEventUri() + "/event");
    }

    public Devoxx2019BECFPClientTest() {
        super(Devoxx2019BECFPClient.class, "friday", "1207", "32351");
    }

    @Test
    @Override
    public void eventsAreRetrievable() {
        ignoreIfServerUnreachable();

        expectedException
                .expect(UnsupportedOperationException.class);
        expectedException
                .expectMessage(CoreMatchers.equalTo(
                        "Devoxx 2019 BE CFP API does not support multiple Events at one CFP location."));

        super.eventsAreRetrievable();
    }
}
