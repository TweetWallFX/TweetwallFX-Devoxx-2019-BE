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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tweetwallfx.controls.WordleSkin;
import org.tweetwallfx.devoxx.cfp.stepengine.dataprovider.SpeakerImageProvider;
import org.tweetwallfx.devoxx19be.provider.ScheduleDataProvider;
import org.tweetwallfx.devoxx19be.provider.SessionData;
import org.tweetwallfx.devoxx19be.provider.TrackImageDataProvider;
import org.tweetwallfx.stepengine.api.DataProvider;
import org.tweetwallfx.stepengine.api.Step;
import org.tweetwallfx.stepengine.api.StepEngine.MachineContext;
import org.tweetwallfx.stepengine.api.config.StepEngineSettings;

/**
 * Devoxx 2019 Show Schedule (Flip In) Animation Step
 *
 * @author Sven Reimers
 */
public class Devoxx19ShowSchedule implements Step {

    private static final Logger LOGGER = LogManager.getLogger(Devoxx19ShowSchedule.class);
    private final Config config;

    private Devoxx19ShowSchedule(Config config) {
        this.config = config;
    }

    @Override
    public void doStep(final MachineContext context) {
        WordleSkin wordleSkin = (WordleSkin) context.get("WordleSkin");
        final ScheduleDataProvider dataProvider = context.getDataProvider(ScheduleDataProvider.class);

        if (null == wordleSkin.getNode().lookup("#scheduleNode")) {
            Pane scheduleNode = new Pane();
            scheduleNode.getStyleClass().add("schedule");
            scheduleNode.setId("scheduleNode");
            scheduleNode.setOpacity(0);

            var title = new Label("Upcoming Talks");

            title.setPrefWidth(config.width);
            title.getStyleClass().add("title");
            title.setPrefHeight(config.titleHeight);
            title.setAlignment(Pos.CENTER);

            scheduleNode.getChildren().add(title);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), scheduleNode);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setOnFinished(e -> {
                LOGGER.info("Calling proceed from Devoxx19SchowSchedule");
                context.proceed();
            });
            scheduleNode.setLayoutX(config.layoutX);
            scheduleNode.setLayoutY(config.layoutY);
            scheduleNode.setMinWidth(config.width);
            scheduleNode.setMaxWidth(config.width);
            scheduleNode.setPrefWidth(config.width);
            scheduleNode.setCacheHint(CacheHint.SPEED);
            scheduleNode.setCache(true);
            int col = 0;
            int row = 0;

            Iterator<SessionData> iterator = dataProvider.getFilteredSessionData().iterator();
            while (iterator.hasNext()) {
                Pane sessionPane = createSessionNode(context, iterator.next());
                double sessionWidth = (config.width - config.sessionHGap) / 2.0;
                sessionPane.setMinWidth(sessionWidth);
                sessionPane.setMaxWidth(sessionWidth);
                sessionPane.setPrefWidth(sessionWidth);
                sessionPane.setMinHeight(config.sessionHeight);
                sessionPane.setMaxHeight(config.sessionHeight);
                sessionPane.setPrefHeight(config.sessionHeight);
                sessionPane.setLayoutX(col * (sessionWidth + config.sessionHGap));
                sessionPane.setLayoutY(config.titleHeight + config.sessionVGap + (config.sessionHeight + config.sessionVGap) * row);
                scheduleNode.getChildren().add(sessionPane);
                col = (col == 0) ? 1 : 0;
                if (col == 0) {
                    row++;
                }
            }

            Platform.runLater(() -> {
                wordleSkin.getPane().getChildren().add(scheduleNode);
                fadeIn.play();
            });
        }
    }

    @Override
    public boolean requiresPlatformThread() {
        return false;
    }

    @Override
    public java.time.Duration preferredStepDuration(final MachineContext context) {
        return java.time.Duration.ofMillis(config.stepDuration);
    }

    private Pane createSessionNode(final MachineContext context, final SessionData sessionData) {
        var speakerNames = new Label(sessionData.speakers.stream().collect(Collectors.joining(", ")));
        speakerNames.setWrapText(true);
        speakerNames.setTextAlignment(TextAlignment.RIGHT);
        speakerNames.getStyleClass().add("speakerName");

        var room = new Label(sessionData.room);
        room.getStyleClass().add("room");

        var times = new Label(sessionData.beginTime + " - " + sessionData.endTime);
        times.getStyleClass().add("times");

        var topLeftVBox = new VBox(4, room, times);
        Pane topLeft = topLeftVBox;

        if (config.showAvatar) {
            var speakerImageProvider = context.getDataProvider(SpeakerImageProvider.class);
            var speakerImages = new HBox(config.avatarSpacing, sessionData.speakerObjects.stream()
                    .map(speakerImageProvider::getSpeakerImage)
                    .map(ImageView::new)
                    .peek(img -> {
                        // general image sizing
                        img.getStyleClass().add("speakerImage");
                        img.setFitHeight(config.avatarSize);
                        img.setFitWidth(config.avatarSize);
                    })
                    .peek(img -> {
                        // avatar image clipping
                        Rectangle clip = new Rectangle(config.avatarSize, config.avatarSize);
                        clip.setArcWidth(config.avatarArcSize);
                        clip.setArcHeight(config.avatarArcSize);
                        img.setClip(clip);
                    })
                    .toArray(Node[]::new)
            );

            topLeft = new HBox(4, topLeftVBox, speakerImages);
        }

        if (config.showFavourite && sessionData.favouritesCount >= 0) {
            final FontAwesomeIconView faiFavCount = new FontAwesomeIconView();
            faiFavCount.getStyleClass().setAll("favoriteGlyph");

            var favLabel = new Label("" + sessionData.favouritesCount);
            favLabel.getStyleClass().setAll("favoriteCount");

            final HBox favourites = new HBox(5, faiFavCount, favLabel);
            favourites.setAlignment(Pos.CENTER_LEFT);

            topLeftVBox.getChildren().add(favourites);
        }

        var title = new Label(sessionData.title);
        title.setWrapText(true);
        title.setAlignment(Pos.BOTTOM_LEFT);
        title.getStyleClass().add("title");
        title.setMaxHeight(Double.MAX_VALUE);

        Node trackImageView;
        if (config.showTrackAvatar && null != sessionData.trackImageUrl) {
            var trackImage = context.getDataProvider(TrackImageDataProvider.class).getImage(sessionData.trackImageUrl);
            trackImageView = new ImageView(trackImage);
        } else {
            trackImageView = null;
        }

        var bpSessionTopPane = new BorderPane();
        bpSessionTopPane.getStyleClass().add("sessionTopPane");
        bpSessionTopPane.setCenter(speakerNames);
        bpSessionTopPane.setLeft(topLeft);
        BorderPane.setAlignment(speakerNames, Pos.TOP_RIGHT);

        var bpTitle = new BorderPane();
        bpTitle.getStyleClass().add("titlePane");
        bpTitle.setBottom(title);

        var bpSessionBottomPane = new BorderPane();
        bpSessionBottomPane.getStyleClass().add("sessionBottomPane");
        bpSessionBottomPane.setRight(trackImageView);
        bpSessionBottomPane.setCenter(bpTitle);

        var bpSessionPane = new BorderPane();
        bpSessionPane.getStyleClass().add("scheduleSession");
        bpSessionPane.setTop(bpSessionTopPane);
        bpSessionPane.setBottom(bpSessionBottomPane);

        if (null != trackImageView) {
            BorderPane.setAlignment(trackImageView, Pos.BOTTOM_RIGHT);
        }

        return bpSessionPane;
    }

    /**
     * Implementation of {@link Step.Factory} as Service implementation creating
     * {@link Devoxx19ShowSchedule}.
     */
    public static final class FactoryImpl implements Step.Factory {

        @Override
        public Devoxx19ShowSchedule create(final StepEngineSettings.StepDefinition stepDefinition) {
            return new Devoxx19ShowSchedule(stepDefinition.getConfig(Config.class));
        }

        @Override
        public Class<Devoxx19ShowSchedule> getStepClass() {
            return Devoxx19ShowSchedule.class;
        }

        @Override
        public Collection<Class<? extends DataProvider>> getRequiredDataProviders(final StepEngineSettings.StepDefinition stepSettings) {
            return Arrays.asList(ScheduleDataProvider.class, SpeakerImageProvider.class, TrackImageDataProvider.class);
        }
    }

    public static class Config extends AbstractConfig {

        public double layoutX = 0;
        public double layoutY = 0;
        public boolean showAvatar = false;
        public int avatarSize = 64;
        public int avatarArcSize = 20;
        public int avatarSpacing = 4;
        public boolean showFavourite = false;
        public double width = 800;
        public double titleHeight = 60;
        public double sessionVGap = 10;
        public double sessionHGap = 10;
        public double sessionHeight = 200;
        public boolean showTrackAvatar = true;
    }
}
