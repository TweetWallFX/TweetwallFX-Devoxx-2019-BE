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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javax.ws.rs.HEAD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tweetwallfx.controls.WordleSkin;
import org.tweetwallfx.devoxx.cfp.stepengine.dataprovider.ScheduleDataProvider;
import org.tweetwallfx.devoxx.cfp.stepengine.dataprovider.SessionData;
import org.tweetwallfx.devoxx.cfp.stepengine.dataprovider.SpeakerImageProvider;
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
            scheduleNode.setId("scheduleNode");
            scheduleNode.setOpacity(0);

            var titel = new Label("Upcoming Talks");

            titel.setPrefWidth(config.width);
            titel.setStyle("-fx-background-color: devoxx_gradient; -fx-background-radius: 10; -fx-text-fill: #060b33; -fx-font-size: 20pt; -fx-font-weight: bold;");
            titel.setPrefHeight(config.titelHeight);
            titel.setAlignment(Pos.CENTER);
            
            scheduleNode.getChildren().add(titel);
            
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
                double sessionWidth = (config.width - config.sessionHGap) / 2.0 ;
                sessionPane.setMinWidth(sessionWidth);
                sessionPane.setMaxWidth(sessionWidth);
                sessionPane.setPrefWidth(sessionWidth);
                sessionPane.setMinHeight(config.sessionHeight);
                sessionPane.setMaxHeight(config.sessionHeight);
                sessionPane.setPrefHeight(config.sessionHeight);
                sessionPane.setLayoutX(col * (sessionWidth + config.sessionHGap) );
                sessionPane.setLayoutY(config.titelHeight + config.sessionVGap + (config.sessionHeight + config.sessionVGap) * row);
                scheduleNode.getChildren().add(sessionPane);
                col = (col == 0) ? 1 : 0;
                if (col == 0) {
                    row++;
                }
            }

            Platform.runLater(() ->  {
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
        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("scheduleSession");

        var speakerNames = new Text(sessionData.speakers.stream().collect(Collectors.joining(", ")));
        speakerNames.getStyleClass().add("speakerName");
        var speakerNamesFlow = new TextFlow(speakerNames);
        speakerNamesFlow.getStyleClass().add("speakers");
        speakerNamesFlow.setTextAlignment(TextAlignment.RIGHT);
        gridPane.add(speakerNamesFlow, 2, 0, 1, 2);
        GridPane.setHalignment(speakerNamesFlow, HPos.RIGHT);
        GridPane.setValignment(speakerNamesFlow, VPos.TOP);
        GridPane.setHgrow(speakerNamesFlow, Priority.ALWAYS);
        GridPane.setVgrow(speakerNamesFlow, Priority.NEVER);

        if (config.showAvatar) {
            var apeakerImageProvider = context.getDataProvider(SpeakerImageProvider.class);
            var speakerImages = new HBox(config.avatarSpacing, sessionData.speakerObjects.stream()
                    .map(apeakerImageProvider::getSpeakerImage)
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
            gridPane.add(speakerImages, 1, 0, 1, 2);
            GridPane.setHgrow(speakerImages, Priority.NEVER);
        }

        final Text room = new Text(sessionData.room);
        room.getStyleClass().add("room");
        gridPane.add(room, 0, 0, 1, 1);

        final Text times = new Text(sessionData.beginTime + " - " + sessionData.endTime);
        times.getStyleClass().add("times");
        gridPane.add(times, 0, 1, 1, 1);
        GridPane.setValignment(times, VPos.BASELINE);
        GridPane.setVgrow(times, Priority.ALWAYS);

        final Text titleText = new Text(sessionData.title);
        final TextFlow title = new TextFlow(titleText);
        title.getStyleClass().add("title");
        gridPane.add(title, 0, 2, 3, 1);
        GridPane.setHalignment(room, HPos.LEFT);
        GridPane.setValignment(room, VPos.BOTTOM);

        return gridPane;
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
            return Arrays.asList(ScheduleDataProvider.class, SpeakerImageProvider.class);
        }
    }

    public static class Config extends AbstractConfig {

        public double layoutX = 0;
        public double layoutY = 0;
        public boolean showAvatar = false;
        public int avatarSize = 64;
        public int avatarArcSize = 20;
        public int avatarSpacing = 4;
        public double width = 800;
        public double titelHeight = 60;
        public double sessionVGap = 10; 
        public double sessionHGap = 10; 
        public double sessionHeight = 200;
    }
}
