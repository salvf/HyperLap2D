/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package games.rednblack.editor.view.stage.tools;

import com.badlogic.gdx.math.Vector2;
import games.rednblack.editor.factory.ItemFactory;
import games.rednblack.editor.renderer.data.LightVO;
import games.rednblack.editor.renderer.factory.EntityFactory;

/**
 * Created by azakhary on 4/30/2015.
 */
public class ConeLightTool extends ItemDropTool {

    public static final String NAME = "CONE_LIGHT_TOOL";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortcut() {
        return null;
    }

    @Override
    public String getTitle() {
        return "Cone Light Tool";
    }

    @Override
    public int putItem(float x, float y) {
        //LayerItemVO layer = Sandbox.getInstance().getItemFactory().getSelectedLayer();
        LightVO vo = new LightVO();
        vo.type = LightVO.LightType.CONE;
        vo.distance = vo.distance / sandbox.getPixelPerWU();
        vo.coneDegree = 45;

        return ItemFactory.get().createLightItem(vo, new Vector2(x, y));
    }

    @Override
    public int[] listItemFilters() {
        int[] filter = {EntityFactory.LIGHT_TYPE};
        return filter;
    }
}
