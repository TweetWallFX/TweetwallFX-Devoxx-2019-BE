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
package org.tweetwallfx.devoxx19be.filter;

import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tweetwallfx.filterchain.FilterChainSettings;
import org.tweetwallfx.filterchain.FilterStep;
import org.tweetwallfx.tweet.api.Tweet;
import static org.tweetwallfx.util.ToString.createToString;
import static org.tweetwallfx.util.ToString.map;

/**
 * A {@link FilterStep} handling {@link Tweet}s by checking that the its text
 * does not contain certain text passages. If it does the {@link Tweet} is
 * rejected.
 *
 * In case the text contains any of the configured text phrases it is terminally
 * rejected with {@link Result#REJECTED}. Otherwise it is evaluated as
 * {@link Result#NOTHING_DEFINITE}.
 */
public class ContainsTextPhraseRejectFilterStep implements FilterStep<Tweet> {

    private static final Logger LOG = LogManager.getLogger(ContainsTextPhraseRejectFilterStep.class);
    private final Config config;

    private ContainsTextPhraseRejectFilterStep(Config config) {
        this.config = config;
    }

    @Override
    public FilterStep.Result check(final Tweet tweet) {
        Tweet t = tweet;

        do {
            LOG.info("Tweet(id:{}): Checking Tweet(id:{}) ...", t.getId(), tweet.getId());

            final String text = t.getText().toLowerCase(Locale.ENGLISH);
            final Optional<String> containedPhrase = config.getTextPhrases().stream()
                    .map(s -> s.toLowerCase(Locale.ENGLISH))
                    .filter(phrase -> text.contains(phrase))
                    .findAny();

            if (containedPhrase.isPresent()) {
                LOG.info("Tweet(id:{}): Rejected Tweet(id:{}) because it contains the phrase {}", t.getId(), tweet.getId(), containedPhrase.get());
                return FilterStep.Result.REJECTED;
            }

            LOG.debug("Tweet(id:{}): None of the rejected text phrases are contained in in Tweet(id:{})", t.getId(), tweet.getId());
            t = t.getRetweetedTweet();
        } while (config.isCheckRetweeted() && null != t);

        LOG.info("Tweet(id:{}): No terminal decision found -> NOTHING_DEFINITE", tweet.getId());
        return FilterStep.Result.NOTHING_DEFINITE;
    }

    /**
     * Implementation of {@link FilterStep.Factory} creating
     * {@link ContainsTextPhraseRejectFilterStep}.
     */
    public static final class FactoryImpl implements FilterStep.Factory {

        @Override
        public Class<Tweet> getDomainObjectClass() {
            return Tweet.class;
        }

        @Override
        public Class<ContainsTextPhraseRejectFilterStep> getFilterStepClass() {
            return ContainsTextPhraseRejectFilterStep.class;
        }

        @Override
        public ContainsTextPhraseRejectFilterStep create(final FilterChainSettings.FilterStepDefinition filterStepDefinition) {
            return new ContainsTextPhraseRejectFilterStep(filterStepDefinition.getConfig(Config.class));
        }
    }

    /**
     * POJO used to configure {@link ContainsTextPhraseRejectFilterStep}.
     */
    public static final class Config {

        private boolean checkRetweeted = true;
        private Set<String> textPhrases = Collections.emptySet();

        /**
         * Returns a boolean flag controlling whether for a retweet the
         * retweeted Tweet is also checked.
         *
         * @return boolean flag controlling whether for a retweet the retweeted
         * Tweet is also checked
         */
        public boolean isCheckRetweeted() {
            return checkRetweeted;
        }

        /**
         * Sets the boolean flag controlling whether for a retweet the retweeted
         * Tweet is also checked.
         *
         * @param checkRetweeted the new value
         */
        public void setCheckRetweeted(final boolean checkRetweeted) {
            this.checkRetweeted = checkRetweeted;
        }

        /**
         * Returns the set of text phrases to check for.
         *
         * @return set of text phrases
         */
        public Set<String> getTextPhrases() {
            return textPhrases;
        }

        /**
         * Sets the new set of text phrases to check for.
         *
         * @param textPhrases the new text phrases
         */
        public void setTextPhrases(final Set<String> textPhrases) {
            this.textPhrases = Objects.requireNonNull(textPhrases, "textPhrases is null");
        }

        @Override
        public String toString() {
            return createToString(this, map(
                    "checkRetweeted", isCheckRetweeted(),
                    "textPhrases", getTextPhrases()
            )) + " extends " + super.toString();
        }
    }
}
