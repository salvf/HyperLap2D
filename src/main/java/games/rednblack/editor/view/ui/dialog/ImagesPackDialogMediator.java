package games.rednblack.editor.view.ui.dialog;

import games.rednblack.editor.HyperLap2DFacade;
import games.rednblack.editor.proxy.ProjectManager;
import games.rednblack.editor.renderer.data.TexturePackVO;
import games.rednblack.editor.view.menu.ResourcesMenu;
import games.rednblack.editor.view.stage.Sandbox;
import games.rednblack.editor.view.stage.UIStage;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

public class ImagesPackDialogMediator extends Mediator<AtlasesPackDialog> {
    private static final String TAG = ImagesPackDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    private static final String NEW_IMAGES_PACK = "games.rednblack.editor.view.ui.dialog.ImagesPackDialogMediator.NEW_IMAGES_PACK";
    private static final String MOVE_REGION_TO_PACK = "games.rednblack.editor.view.ui.dialog.ImagesPackDialogMediator.MOVE_REGION_TO_PACK";
    private static final String UPDATE_CURRENT_LIST = "games.rednblack.editor.view.ui.dialog.ImagesPackDialogMediator.UPDATE_CURRENT_LIST";

    public ImagesPackDialogMediator() {
        super(NAME, new AtlasesPackDialog("Images Atlases", NEW_IMAGES_PACK, MOVE_REGION_TO_PACK, UPDATE_CURRENT_LIST));
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = HyperLap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ResourcesMenu.OPEN_IMAGES_PACK,
                ProjectManager.PROJECT_OPENED,
                NEW_IMAGES_PACK,
                MOVE_REGION_TO_PACK,
                UPDATE_CURRENT_LIST
        };
    }

    @Override
    public void handleNotification(INotification notification) {
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);

        switch (notification.getName()) {
            case ResourcesMenu.OPEN_IMAGES_PACK:
                viewComponent.show(uiStage);
                break;
            case ProjectManager.PROJECT_OPENED:
                viewComponent.initPacks(projectManager.currentProjectInfoVO.imagesPacks.keySet());
                viewComponent.updateMainPack(projectManager.currentProjectInfoVO.imagesPacks.get("main").regions);
                break;
            case NEW_IMAGES_PACK:
                TexturePackVO newVo = new TexturePackVO();
                newVo.name = notification.getBody();

                projectManager.currentProjectInfoVO.imagesPacks.put(newVo.name, newVo);

                viewComponent.addNewPack(newVo.name);
                break;
            case UPDATE_CURRENT_LIST:
                String currentTab = viewComponent.getSelectedTab();
                viewComponent.updateCurrentPack(projectManager.currentProjectInfoVO.imagesPacks.get(currentTab).regions);
                break;
            case MOVE_REGION_TO_PACK:
                String toPack = viewComponent.getMainSelected() != null ? viewComponent.getSelectedTab() : "main";
                String fromPack = viewComponent.getMainSelected() == null ? viewComponent.getSelectedTab() : "main";
                String region = viewComponent.getMainSelected() != null ? viewComponent.getMainSelected() : viewComponent.getCurrentSelected();

                projectManager.currentProjectInfoVO.imagesPacks.get(fromPack).regions.remove(region);
                projectManager.currentProjectInfoVO.imagesPacks.get(toPack).regions.add(region);

                viewComponent.updateCurrentPack(projectManager.currentProjectInfoVO.imagesPacks.get(viewComponent.getSelectedTab()).regions);
                viewComponent.updateMainPack(projectManager.currentProjectInfoVO.imagesPacks.get("main").regions);
                break;
        }
    }
}