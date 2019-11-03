/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweetwallfx.devoxx19be.steps;

import java.util.Collection;
import java.util.Collections;
import org.tweetwallfx.stepengine.api.DataProvider;
import org.tweetwallfx.stepengine.api.Step;
import org.tweetwallfx.stepengine.api.StepEngine;
import org.tweetwallfx.stepengine.api.config.StepEngineSettings;

/**
 *
 * @author sven
 */
public class GenericShutdownStep implements Step{

    private final Config config;

    private GenericShutdownStep(Config config) {
        this.config = config;
    }

    @Override
    public void doStep(StepEngine.MachineContext context) {
        context.get(config.stepToTerminate, Controllable.class).shutdown();
        context.proceed();
    }

    @Override
    public boolean requiresPlatformThread() {
        return false;
    }   
    
        /**
     * Implementation of {@link Step.Factory} as Service implementation creating
     * {@link Devoxx19FlipInTweets}.
     */
    public static final class FactoryImpl implements Step.Factory {

        @Override
        public GenericShutdownStep create(final StepEngineSettings.StepDefinition stepDefinition) {
            return new GenericShutdownStep(stepDefinition.getConfig(Config.class));
        }

        @Override
        public Class<GenericShutdownStep> getStepClass() {
            return GenericShutdownStep.class;
        }        
        
        @Override
        public Collection<Class<? extends DataProvider>> getRequiredDataProviders(final StepEngineSettings.StepDefinition stepSettings) {
            return Collections.emptyList();
        }
    }

    public static class Config extends AbstractConfig {

        public String stepToTerminate = "Not defined - please set in configuration";
    }
}
