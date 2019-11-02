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
package org.tweetwallfx.devoxx19be.steps.mosaic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.CacheHint;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.tweetwallfx.controls.WordleSkin;
import org.tweetwallfx.devoxx19be.steps.AbstractConfig;
import org.tweetwallfx.stepengine.api.DataProvider;
import org.tweetwallfx.stepengine.api.Step;
import org.tweetwallfx.stepengine.api.StepEngine.MachineContext;
import org.tweetwallfx.stepengine.api.config.StepEngineSettings;
import org.tweetwallfx.stepengine.dataproviders.ImageMosaicDataProvider;
import org.tweetwallfx.stepengine.dataproviders.ImageMosaicDataProvider.ImageStore;

public class ImageMosaicCreateStep implements Step {

    private final Config config;
    private static final Random RANDOM = new Random();
    private ImageView[][] rects;
    private Bounds[][] bounds;
    private Pane pane;

    private ImageMosaicCreateStep(Config config) {
        this.config = config;
        rects = new ImageView[config.columns][config.rows];
        bounds = new Bounds[config.columns][config.rows];
    }

    @Override
    public boolean shouldSkip(MachineContext context) {
        ImageMosaicDataProvider dataProvider = context.getDataProvider(ImageMosaicDataProvider.class);
        System.err.println("Images loaded: " + dataProvider.getImages().size());
        return dataProvider.getImages().size() < config.minNoImages;
    }

    @Override
    public void doStep(final MachineContext context) {
        WordleSkin wordleSkin = (WordleSkin) context.get("WordleSkin");

        context.put("mosaicImages", rects);
        context.put("mosaicBounds", bounds);
        context.put("mosaicConfig", config);

        ImageMosaicDataProvider dataProvider = context.getDataProvider(ImageMosaicDataProvider.class);
        pane = wordleSkin.getPane();
        Transition createMosaicTransition = createMosaicTransition(dataProvider.getImages());
        createMosaicTransition.setOnFinished(e -> context.proceed());
        createMosaicTransition.play();
    }

    @Override
    public java.time.Duration preferredStepDuration(MachineContext context) {
        return java.time.Duration.ofMillis(config.stepDuration);
    }

    private Transition createMosaicTransition(final List<ImageStore> imageStores) {
        final SequentialTransition fadeIn = new SequentialTransition();
        final List<FadeTransition> allFadeIns = new ArrayList<>();
        final double width = ((null != config.width) ? config.width : pane.getWidth()) / (double) config.columns - 10;
        final double height = ((null != config.height) ? config.height : pane.getHeight()) / (double) config.rows - 8;
        final List<ImageStore> distillingList = new ArrayList<>(imageStores);

        for (int i = 0; i < config.columns; i++) {
            for (int j = 0; j < config.rows; j++) {
                int index = RANDOM.nextInt(distillingList.size());
                ImageStore selectedImage = distillingList.remove(index);
                ImageView imageView = new ImageView(selectedImage.getImage());
                imageView.setId("tweetImage");
                imageView.setCache(true);
                imageView.setCacheHint(CacheHint.QUALITY);
                imageView.setFitWidth(width);
                imageView.setFitHeight(height);
                imageView.setEffect(new GaussianBlur(0));
                rects[i][j] = imageView;
                bounds[i][j] = new BoundingBox(i * (width + 10) + 5, j * (height + 8) + 4, width, height);
                rects[i][j].setOpacity(0);
                rects[i][j].setLayoutX(bounds[i][j].getMinX() + config.layoutX);
                rects[i][j].setLayoutY(bounds[i][j].getMinY() + config.layoutY);
                pane.getChildren().add(rects[i][j]);
                FadeTransition ft = new FadeTransition(Duration.seconds(0.3), imageView);
                ft.setToValue(1);
                allFadeIns.add(ft);
            }
        }
        Collections.shuffle(allFadeIns);
        fadeIn.getChildren().addAll(allFadeIns);
        return fadeIn;
    }

    /**
     * Implementation of {@link Step.Factory} as Service implementation creating
     * {@link ImageMosaicCreateStep}.
     */
    public static final class FactoryImpl implements Step.Factory {

        @Override
        public ImageMosaicCreateStep create(final StepEngineSettings.StepDefinition stepDefinition) {
            return new ImageMosaicCreateStep(stepDefinition.getConfig(Config.class));
        }

        @Override
        public Class<ImageMosaicCreateStep> getStepClass() {
            return ImageMosaicCreateStep.class;
        }

        @Override
        public Collection<Class<? extends DataProvider>> getRequiredDataProviders(final StepEngineSettings.StepDefinition stepSettings) {
            return Arrays.asList(ImageMosaicDataProvider.class);
        }
    }

    public static class Config extends AbstractConfig {

        public Double width = null;
        public Double height = null;
        public double layoutX = 0;
        public double layoutY = 0;
        public int rows = 6;
        public int columns = 5;
        public int minNoImages = 30;
    }
}
