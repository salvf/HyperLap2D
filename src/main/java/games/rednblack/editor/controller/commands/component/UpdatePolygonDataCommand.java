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

package games.rednblack.editor.controller.commands.component;

import com.badlogic.gdx.math.Vector2;
import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.controller.commands.EntityModifyRevertibleCommand;
import games.rednblack.editor.renderer.components.PolygonComponent;
import games.rednblack.editor.utils.runtime.EntityUtils;
import games.rednblack.editor.utils.runtime.SandboxComponentRetriever;
import games.rednblack.h2d.common.MsgAPI;

/**
 * Created by azakhary on 7/3/2015.
 */
public class UpdatePolygonDataCommand extends EntityModifyRevertibleCommand {

    private Integer entityId;
    private Vector2[][] dataFrom;
    private Vector2[][] dataTo;

    private void collectData() {
        Object[] payload = getNotification().getBody();
        entityId = EntityUtils.getEntityId((int) payload[0]);
        dataFrom = (Vector2[][]) payload[1];
        dataTo = (Vector2[][]) payload[2];
        dataFrom = dataFrom.clone();
        dataTo = dataTo.clone();
    }

    @Override
    public void doAction() {
        collectData();

        int entity = EntityUtils.getByUniqueId(entityId);

        PolygonComponent polygonComponent = SandboxComponentRetriever.get(entity, PolygonComponent.class);
        polygonComponent.vertices = dataTo;

        EntityUtils.refreshComponents(entity);

        HyperLap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);

    }

    @Override
    public void undoAction() {
        int entity = EntityUtils.getByUniqueId(entityId);

        PolygonComponent polygonComponent = SandboxComponentRetriever.get(entity, PolygonComponent.class);
        polygonComponent.vertices = dataFrom;

        EntityUtils.refreshComponents(entity);

        HyperLap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    public static Object[] payloadInitialState(int entity) {
        PolygonComponent polygonComponent = SandboxComponentRetriever.get(entity, PolygonComponent.class);
        Object[] payload = new Object[3];
        payload[0] = entity;
        payload[1] = cloneData(polygonComponent.vertices);

        return payload;
    }

    public static Object[] payload(Object[] payload, Vector2[][] vertices) {
        payload[2] = cloneData(vertices);

        return payload;
    }

    private static Vector2[][] cloneData(Vector2[][] data) {
        Vector2[][] newData = new Vector2[data.length][];
        for(int i = 0; i < data.length; i++) {
            newData[i] = new Vector2[data[i].length];
            for(int j = 0; j < data[i].length; j++) {
                newData[i][j] = data[i][j].cpy();
            }
        }

        return newData;
    }
}
