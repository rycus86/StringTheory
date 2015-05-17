package hu.rycus.watchface.stringtheory;

import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;

import java.util.Collection;

import hu.rycus.watchface.commons.BaseCanvasWatchFaceService;
import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.stringtheory.components.BlackBackground;
import hu.rycus.watchface.stringtheory.components.Date;
import hu.rycus.watchface.stringtheory.components.HourAndMinute;
import hu.rycus.watchface.stringtheory.components.StringsBackground;
import hu.rycus.watchface.stringtheory.config.Configuration;

public class StringTheoryWatchFace extends BaseCanvasWatchFaceService {

    @Override
    public BaseEngine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends BaseEngine {

        @Override
        protected String[] getConfigurationPaths() {
            return new String[] {Configuration.PATH};
        }

        @Override
        protected void createComponents(final Collection<Component> components) {
            components.add(new BlackBackground());
            components.add(new StringsBackground());
            components.add(new HourAndMinute());
            components.add(new Date());
        }

        @Override
        protected WatchFaceStyle buildStyle(final WatchFaceStyle.Builder builder) {
            return builder
                    .setStatusBarGravity(Gravity.RIGHT | Gravity.TOP)
                    .setHotwordIndicatorGravity(Gravity.CENTER)
                    .setViewProtection(WatchFaceStyle.PROTECT_HOTWORD_INDICATOR)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .build();
        }

    }

}
