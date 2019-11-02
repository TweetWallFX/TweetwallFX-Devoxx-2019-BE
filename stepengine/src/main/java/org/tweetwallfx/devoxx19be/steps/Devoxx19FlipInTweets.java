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
package org.tweetwallfx.devoxx19be.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.tweetwallfx.controls.WordleSkin;
import org.tweetwallfx.stepengine.api.DataProvider;
import org.tweetwallfx.stepengine.api.Step;
import org.tweetwallfx.stepengine.api.StepEngine.MachineContext;
import org.tweetwallfx.stepengine.api.config.StepEngineSettings;
import org.tweetwallfx.stepengine.dataproviders.TweetUserProfileImageDataProvider;
import org.tweetwallfx.transitions.FlipInXTransition;
import org.tweetwallfx.tweet.api.Tweet;
import org.tweetwallfx.tweet.stepengine.dataprovider.TweetStreamDataProvider;

/**
 * Devox 2019 TweetStream Flip In Animation Step
 *
 * @author Sven Reimers
 */
public class Devoxx19FlipInTweets implements Step {

    private final Config config;

    protected Devoxx19FlipInTweets(Config config) {
        this.config = config;
    }

    @Override
    public void doStep(final MachineContext context) {
        WordleSkin wordleSkin = (WordleSkin) context.get("WordleSkin");
        final TweetStreamDataProvider dataProvider = context.getDataProvider(TweetStreamDataProvider.class);

        VBox tweetList = getOrCreateTweetList(wordleSkin);

        List<Transition> transitions = new ArrayList<>();

//        tweetList.layoutXProperty().bind(Bindings.multiply(1000.0 / 1920.0, wordleSkin.getSkinnable().widthProperty()));
//        tweetList.layoutYProperty().bind(Bindings.multiply(200.0 / 1280.0, wordleSkin.getSkinnable().heightProperty()));

        tweetList.setLayoutX(config.layoutX);
        tweetList.setLayoutY(config.layoutY);

        List<Tweet> tweets = dataProvider.getTweets();
        for (int i = 0; i < Math.min(tweets.size(), config.numberOfTweetsToDisplay); i++) {
            HBox tweet = createSingleTweetDisplay(tweets.get(i), context, config.tweetWidth);
            tweet.setMaxWidth(config.tweetWidth + 64 + 10);
            tweet.getStyleClass().add("tweetDisplay");
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2);
            dropShadow.setOffsetY(2);
            dropShadow.setColor(Color.web("#060b33"));
            tweet.setEffect(dropShadow);
            transitions.add(new FlipInXTransition(tweet));
            tweetList.getChildren().add(tweet);
            VBox.setMargin(tweet, new Insets(0, 0, config.tweetGap, 0));
//            if (i < 5 && i != 1) {
//                Pane pane = new Pane();
//                pane.getChildren().add(new Line(0, 0, maxWidth[i], 0));
//                pane.setPadding(new Insets(spacing[i] / 2., 0, spacing[i] / 2., 0));
//                tweetList.getChildren().add(pane);
//            }
        }
        ParallelTransition flipIns = new ParallelTransition();
        flipIns.getChildren().addAll(transitions);
        flipIns.setOnFinished(e -> context.proceed());

        flipIns.play();
    }

    private VBox getOrCreateTweetList(final WordleSkin wordleSkin) {
        VBox vbox = (VBox) wordleSkin.getNode().lookup("#tweetList");
        if (null == vbox) {
            vbox = new VBox();
            vbox.setId("tweetList");
            wordleSkin.getPane().getChildren().add(vbox);
        }
        return vbox;
    }

    private HBox createSingleTweetDisplay(
            final Tweet displayTweet,
            final MachineContext context,
            final double maxWidth) {
        String textWithoutMediaUrls = displayTweet.getDisplayEnhancedText();
        Text text = new Text(textWithoutMediaUrls.replaceAll("[\n\r]", "|"));
        text.setCache(true);
        text.setCacheHint(CacheHint.SPEED);
        text.getStyleClass().add("tweetText");
        Image profileImage = context.getDataProvider(TweetUserProfileImageDataProvider.class).getImageBig(displayTweet.getUser());
        BorderPane imageViewPane = new BorderPane();
        imageViewPane.getStyleClass().add("tweetProfileImage");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setSmooth(true);
        profileImageView.setCacheHint(CacheHint.QUALITY);
        imageViewPane.setCenter(profileImageView);
        BorderPane.setMargin(profileImageView, new Insets(0,0,0,5));
        TextFlow flow = new TextFlow(text);
        flow.getStyleClass().add("tweetFlow");
        flow.maxWidthProperty().set(maxWidth);
        flow.maxHeightProperty().set(70);
        flow.minHeightProperty().set(70);
        flow.setCache(true);
        flow.setCacheHint(CacheHint.SPEED);
        Text name = new Text(displayTweet.getUser().getName());
        name.getStyleClass().add("tweetUsername");
        name.setCache(true);
        name.setCacheHint(CacheHint.SPEED);
        HBox tweet = new HBox(imageViewPane, new VBox(name, flow));
        VBox.setMargin(name, new Insets(2,0,0,0));
        tweet.setCacheHint(CacheHint.QUALITY);
        tweet.setSpacing(10);
        return tweet;
    }

    @Override
    public java.time.Duration preferredStepDuration(final MachineContext context) {
        return java.time.Duration.ofMillis(config.stepDuration);
    }

    /**
     * Implementation of {@link Step.Factory} as Service implementation creating
     * {@link Devoxx19FlipInTweets}.
     */
    public static final class FactoryImpl implements Step.Factory {

        @Override
        public Devoxx19FlipInTweets create(final StepEngineSettings.StepDefinition stepDefinition) {
            return new Devoxx19FlipInTweets(stepDefinition.getConfig(Config.class));
        }

        @Override
        public Class<Devoxx19FlipInTweets> getStepClass() {
            return Devoxx19FlipInTweets.class;
        }

        @Override
        public Collection<Class<? extends DataProvider>> getRequiredDataProviders(final StepEngineSettings.StepDefinition stepSettings) {
            return Arrays.asList(
                    TweetStreamDataProvider.class,
                    TweetUserProfileImageDataProvider.class
            );
        }
    }

    public static class Config extends AbstractConfig {

        public double layoutX = 0;
        public double layoutY = 0;
        public double numberOfTweetsToDisplay = 7;
        public double tweetWidth = 600;
        public double tweetGap = 20;
    }
}
