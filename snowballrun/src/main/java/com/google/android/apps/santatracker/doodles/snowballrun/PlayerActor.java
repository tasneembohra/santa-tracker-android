/*
 * Copyright 2019. Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.apps.santatracker.doodles.snowballrun;

import android.content.res.Resources;
import com.google.android.apps.santatracker.doodles.shared.animation.AnimatedSprite;

/**
 * The player actor that has two more animation states than runner actor: celebrating running and
 * sweating running.
 */
public class PlayerActor extends RunnerActor {
    protected AnimatedSprite celebrateSprite;
    protected AnimatedSprite sweatSprite;
    boolean isSweating;
    boolean isCelebrating;

    public PlayerActor(Resources resources, int lane) {
        super(resources, RunnerType.PLAYER, lane);
        isSweating = false;
        isCelebrating = false;

        sweatSprite =
                AnimatedSprite.fromFrames(resources, SnowballRunSprites.snowballrun_running_losing);
        celebrateSprite =
                AnimatedSprite.fromFrames(resources, SnowballRunSprites.snowballrun_running_normal);

        setSpriteAnchorUpright(sweatSprite);
        setSpriteAnchorUpright(celebrateSprite);
    }

    @Override
    public void setRunnerState(RunnerState state) {
        super.setRunnerState(state);
        if (state == RunnerState.RUNNING) {
            if (isCelebrating) {
                currentSprite = celebrateSprite;
            } else if (isSweating) {
                currentSprite = sweatSprite;
            } else {
                currentSprite = runningSprite;
            }
        }
    }

    public void setSweat(boolean sweat) {
        if (this.isSweating == sweat) {
            return;
        }

        this.isSweating = sweat;

        if (state != RunnerState.RUNNING || isCelebrating) {
            return;
        }

        if (sweat) {
            sweatSprite.setFrameIndex(runningSprite.getFrameIndex());
            currentSprite = sweatSprite;
        } else {
            runningSprite.setFrameIndex(sweatSprite.getFrameIndex() % runningSprite.getNumFrames());
            currentSprite = runningSprite;
        }
    }

    public void setCelebrate(boolean celebrate) {
        if (this.isCelebrating == celebrate) {
            return;
        }

        this.isCelebrating = celebrate;

        if (state != RunnerState.RUNNING) {
            return;
        }

        if (celebrate) {
            celebrateSprite.setFrameIndex(runningSprite.getFrameIndex());
            currentSprite = celebrateSprite;
        } else {
            runningSprite.setFrameIndex(
                    celebrateSprite.getFrameIndex() % runningSprite.getNumFrames());
            currentSprite = runningSprite;
        }
    }
}
