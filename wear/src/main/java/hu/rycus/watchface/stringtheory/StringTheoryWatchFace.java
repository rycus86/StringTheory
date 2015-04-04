package hu.rycus.watchface.stringtheory;

import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;

import java.util.Collection;

import hu.rycus.watchface.commons.BaseCanvasWatchFaceService;
import hu.rycus.watchface.commons.BlackAmbientBackground;
import hu.rycus.watchface.commons.Component;
import hu.rycus.watchface.stringtheory.components.Background;
import hu.rycus.watchface.stringtheory.components.HourAndMinute;

public class StringTheoryWatchFace extends BaseCanvasWatchFaceService {

    @Override
    public BaseEngine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends BaseEngine {

        @Override
        protected String[] getConfigurationPaths() {
            return new String[0];
        }

        @Override
        protected void createComponents(final Collection<Component> components) {
            components.add(new BlackAmbientBackground());
            components.add(new Background());
            components.add(new HourAndMinute(getAssets()));
            // TODO
        }

        @Override
        protected WatchFaceStyle buildStyle(final WatchFaceStyle.Builder builder) {
            return builder
                    .setStatusBarGravity(Gravity.RIGHT | Gravity.TOP)
                    .setHotwordIndicatorGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                    .setViewProtection(
                            WatchFaceStyle.PROTECT_STATUS_BAR |
                            WatchFaceStyle.PROTECT_HOTWORD_INDICATOR)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .build(); // TODO
        }

    }

}
