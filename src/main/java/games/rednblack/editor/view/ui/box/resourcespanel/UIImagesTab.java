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

package games.rednblack.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.view.ui.box.UIResourcesBoxMediator;
import games.rednblack.editor.view.ui.box.resourcespanel.draggable.DraggableResource;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIImagesTab extends UIResourcesTab {

    private VisTable imagesTable;

    public UIImagesTab() {
        super();
    }

    @Override
    protected VisScrollPane crateScrollPane() {
        imagesTable = new VisTable();
        HyperLap2DFacade.getInstance().sendNotification(UIResourcesBoxMediator.ADD_RESOURCES_BOX_TABLE_SELECTION_MANAGEMENT, imagesTable);
        return StandardWidgetsFactory.createScrollPane(imagesTable);
    }

    @Override
    public String getTabTitle() {
        return "Images";
    }

    @Override
    public String getTabIconStyle() {
        return "image-button";
    }

    public void setThumbnailBoxes(Array<DraggableResource> draggableResources) {
        imagesTable.clearChildren();
        for (int i = 0; i < draggableResources.size; i++) {
            DraggableResource draggableResource = draggableResources.get(i);

            imagesTable.add((Actor) draggableResource.getViewComponent()).padRight(5).padBottom(5);
            if ((i - 7) % 4 == 0) {
                imagesTable.row();
            }
        }
    }
}
