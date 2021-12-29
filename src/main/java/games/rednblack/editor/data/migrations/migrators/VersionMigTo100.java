package games.rednblack.editor.data.migrations.migrators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import games.rednblack.editor.data.migrations.IVersionMigrator;
import games.rednblack.editor.data.migrations.data020.CompositeVO;
import games.rednblack.editor.renderer.data.*;
import games.rednblack.editor.renderer.utils.PolygonUtils;
import games.rednblack.h2d.common.vo.ProjectVO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class VersionMigTo100 implements IVersionMigrator {
    private final Json json = new Json();

    private String projectPath;
    private ProjectVO projectVO;
    private games.rednblack.editor.data.migrations.data020.ProjectInfoVO projectInfoVO;

    @Override
    public void setProject(String path, ProjectVO vo, ProjectInfoVO projectInfoVO) {
        projectPath = path;
        projectVO = vo;

        json.setOutputType(JsonWriter.OutputType.json);
        String prjInfoFilePath = projectPath + "/project.dt";
        FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
        String projectInfoContents = "{}";
        try {
            projectInfoContents = FileUtils.readFileToString(projectInfoFile.file(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.projectInfoVO = json.fromJson(games.rednblack.editor.data.migrations.data020.ProjectInfoVO.class, projectInfoContents);
    }

    @Override
    public boolean doMigration() {
        String srcPath = projectPath + File.separator + "scenes";
        FileHandle scenesDirectoryHandle = Gdx.files.absolute(srcPath);
        try {
            for (File scene : scenesDirectoryHandle.file().listFiles()) {
                games.rednblack.editor.data.migrations.data020.SceneVO sceneToExport = json.fromJson(games.rednblack.editor.data.migrations.data020.SceneVO.class, FileUtils.readFileToString(scene, "utf-8"));

                SceneVO newVO = new SceneVO();
                newVO.sceneName = sceneToExport.sceneName;
                newVO.lightsPropertiesVO = sceneToExport.lightsPropertiesVO;
                newVO.physicsPropertiesVO = sceneToExport.physicsPropertiesVO;
                newVO.verticalGuides = sceneToExport.verticalGuides;
                newVO.horizontalGuides = sceneToExport.horizontalGuides;
                CompositeItemVO compositeItemVO = new CompositeItemVO();
                compositeItemVO.automaticResize = false;
                newVO.composite = compositeItemVO;

                copyRecursiveElements(sceneToExport.composite, newVO.composite);

                FileUtils.writeStringToFile(new File(projectPath + File.separator + "scenes" + File.separator + newVO.sceneName + ".dt"),
                        newVO.constructJsonString(), "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProjectInfoVO newProjectInfoVO = new ProjectInfoVO();
        newProjectInfoVO.pixelToWorld = projectInfoVO.pixelToWorld;
        newProjectInfoVO.originalResolution = projectInfoVO.originalResolution;
        newProjectInfoVO.resolutions = projectInfoVO.resolutions;
        for (games.rednblack.editor.data.migrations.data020.SceneVO sceneVO : projectInfoVO.scenes) {
            SceneVO newSceneVO = new SceneVO();
            newSceneVO.sceneName = sceneVO.sceneName;
            newProjectInfoVO.scenes.add(newSceneVO);
        }
        newProjectInfoVO.libraryActions = projectInfoVO.libraryActions;
        newProjectInfoVO.imagesPacks = projectInfoVO.imagesPacks;
        newProjectInfoVO.animationsPacks = projectInfoVO.animationsPacks;

        HashMap<String, games.rednblack.editor.data.migrations.data020.CompositeItemVO> libraryItems = projectInfoVO.libraryItems;
        for (String key : libraryItems.keySet()) {
            games.rednblack.editor.data.migrations.data020.CompositeItemVO item = libraryItems.get(key);
            CompositeItemVO newLibraryItem = new CompositeItemVO();
            copyMainItemField(item, newLibraryItem);
            newLibraryItem.width = item.width;
            newLibraryItem.height = item.height;
            newLibraryItem.automaticResize = item.automaticResize;
            newLibraryItem.scissorsEnabled = item.scissorsEnabled;
            newLibraryItem.renderToFBO = item.renderToFBO;
            newProjectInfoVO.libraryItems.put(key, newLibraryItem);
            copyRecursiveElements(item.composite, newLibraryItem);
        }

        try {
            FileUtils.writeStringToFile(new File(projectPath + File.separator + "project.dt"),
                    newProjectInfoVO.constructJsonString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void copyRecursiveElements(CompositeVO vo, CompositeItemVO target) {
        for (int i = 0; i < vo.sImages.size(); i++) {
            target.addItem(vo.sImages.get(i));
        }
        for (int i = 0; i < vo.sImage9patchs.size(); i++) {
            target.addItem(vo.sImage9patchs.get(i));
        }
        for (int i = 0; i < vo.sLabels.size(); i++) {
            target.addItem(vo.sLabels.get(i));
        }
        for (int i = 0; i < vo.sParticleEffects.size(); i++) {
            target.addItem(vo.sParticleEffects.get(i));
        }
        for (int i = 0; i < vo.sTalosVFX.size(); i++) {
            target.addItem(vo.sTalosVFX.get(i));
        }
        for (int i = 0; i < vo.sLights.size(); i++) {
            target.addItem(vo.sLights.get(i));
        }
        for (int i = 0; i < vo.sSpineAnimations.size(); i++) {
            target.addItem(vo.sSpineAnimations.get(i));
        }
        for (int i = 0; i < vo.sSpriteAnimations.size(); i++) {
            target.addItem(vo.sSpriteAnimations.get(i));
        }
        for (int i = 0; i < vo.sColorPrimitives.size(); i++) {
            target.addItem(vo.sColorPrimitives.get(i));
        }
        for (String key : vo.sStickyNotes.keySet()) {
            target.sStickyNotes.put(key, vo.sStickyNotes.get(key));
        }
        for (int i = 0; i < vo.layers.size(); i++) {
            target.layers.add(vo.layers.get(i));
        }
        for (MainItemVO mainItemVO : target.getAllItems()) {
            if(mainItemVO.shape != null) {
                target.shape = new PolygonShapeVO();
                target.shape.polygonizedVertices = mainItemVO.shape.polygons;
                target.shape.vertices = new Array<>(PolygonUtils.mergeTouchingPolygonsToOne(mainItemVO.shape.polygons));
            }
        }
        for (int i = 0; i < vo.sComposites.size(); i++) {
            games.rednblack.editor.data.migrations.data020.CompositeItemVO compositeItemVO = vo.sComposites.get(i);
            CompositeItemVO newCompositeItemVO = new CompositeItemVO();
            copyMainItemField(compositeItemVO, newCompositeItemVO);
            newCompositeItemVO.width = compositeItemVO.width;
            newCompositeItemVO.height = compositeItemVO.height;
            newCompositeItemVO.automaticResize = compositeItemVO.automaticResize;
            newCompositeItemVO.scissorsEnabled = compositeItemVO.scissorsEnabled;
            newCompositeItemVO.renderToFBO = compositeItemVO.renderToFBO;
            target.addItem(newCompositeItemVO);
            copyRecursiveElements(compositeItemVO.composite, newCompositeItemVO);
        }
    }

    private void copyMainItemField(MainItemVO vo, MainItemVO target) {
        target.uniqueId = vo.uniqueId;
        target.itemIdentifier = vo.itemIdentifier;
        target.itemName = vo.itemName;
        if(vo.tags != null) target.tags = Arrays.copyOf(vo.tags, vo.tags.length);
        target.customVars = vo.customVars;
        target.x = vo.x;
        target.y = vo.y;
        target.rotation = vo.rotation;
        target.zIndex = vo.zIndex;
        target.layerName = vo.layerName;
        if(vo.tint != null) target.tint = Arrays.copyOf(vo.tint, vo.tint.length);
        target.scaleX = vo.scaleX;
        target.scaleY = vo.scaleY;
        target.originX = vo.originX;
        target.originY = vo.originY;
        target.flipX = vo.flipX;
        target.flipY = vo.flipY;

        if(vo.shape != null) {
            target.shape = new PolygonShapeVO();
            target.shape.polygonizedVertices = vo.shape.polygons;
            target.shape.vertices = new Array<>(PolygonUtils.mergeTouchingPolygonsToOne(vo.shape.polygons));
        }

        if(vo.circle != null) {
            target.circle = new Circle(vo.circle);
        }

        if(vo.physics != null){
            target.physics = new PhysicsBodyDataVO(vo.physics);
        }

        if (vo.sensor != null) {
            target.sensor = new SensorDataVO(vo.sensor);
        }

        if(vo.light != null){
            target.light = new LightBodyDataVO(vo.light);
        }

        target.shaderName = vo.shaderName;
        target.shaderUniforms.clear();
        target.shaderUniforms.putAll(vo.shaderUniforms);

        target.renderingLayer = vo.renderingLayer;
    }
}
