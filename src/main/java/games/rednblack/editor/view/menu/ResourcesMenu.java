package games.rednblack.editor.view.menu;

import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.MenuItem;
import games.rednblack.editor.event.MenuItemListener;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.editor.utils.KeyBindingsLayout;
import games.rednblack.h2d.common.MsgAPI;

import static games.rednblack.h2d.common.MenuAPI.FILE_MENU;
import static games.rednblack.h2d.common.MenuAPI.RESOURCE_MENU;

public class ResourcesMenu extends H2DMenu {

    public static final String IMPORT_TO_LIBRARY = HyperLap2DMenuBar.prefix + ".IMPORT_TO_LIBRARY";
    public static final String IMPORT_SPRITE_SHEET = HyperLap2DMenuBar.prefix + ".IMPORT_SPRITE_SHEET";
    public static final String CREATE_PLACEHOLDER = HyperLap2DMenuBar.prefix + ".CREATE_PLACEHOLDER";
    public static final String CREATE_NOISE = HyperLap2DMenuBar.prefix + ".CREATE_NOISE";
    public static final String OPEN_IMAGES_PACK = HyperLap2DMenuBar.prefix + ".OPEN_IMAGES_PACK";
    public static final String OPEN_ANIMATIONS_PACK = HyperLap2DMenuBar.prefix + ".OPEN_ANIMATIONS_PACK";
    public static final String OPEN_SHADER_MANAGER = HyperLap2DMenuBar.prefix + ".OPEN_SHADER_MANAGER";

    public ResourcesMenu() {
        super(SettingsManager.translationVO.menu.RESOURCES);
        MenuItem importToLibrary = new MenuItem(SettingsManager.translationVO.menu.IMPORTRESOURCES, new MenuItemListener(IMPORT_TO_LIBRARY, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.IMPORT_TO_LIBRARY));
        MenuItem importSpriteSheet = new MenuItem(SettingsManager.translationVO.menu.IMPORTSPRITE, new MenuItemListener(IMPORT_SPRITE_SHEET, null, RESOURCE_MENU));
        MenuItem placeholders = new MenuItem(SettingsManager.translationVO.menu.CREATEPLACEHOLDER, new MenuItemListener(CREATE_PLACEHOLDER, null, RESOURCE_MENU));
        MenuItem noise = new MenuItem(SettingsManager.translationVO.menu.CREATEPERLINNOISE, new MenuItemListener(CREATE_NOISE, null, RESOURCE_MENU));
        MenuItem repack = new MenuItem(SettingsManager.translationVO.menu.REPACKASSETS, new MenuItemListener(MsgAPI.ACTION_REPACK, null, RESOURCE_MENU));
        MenuItem imagesPack = new MenuItem(SettingsManager.translationVO.menu.IMAGESATLASES, new MenuItemListener(OPEN_IMAGES_PACK, null, RESOURCE_MENU));
        MenuItem animationsPack = new MenuItem(SettingsManager.translationVO.menu.ANIMATIONSATLASES, new MenuItemListener(OPEN_ANIMATIONS_PACK, null, RESOURCE_MENU));
        MenuItem shaderManager = new MenuItem(SettingsManager.translationVO.menu.SHADERMANAGER, new MenuItemListener(OPEN_SHADER_MANAGER, null, RESOURCE_MENU));

        addItem(importToLibrary);
        addSeparator();
        addItem(repack);
        addSeparator();
        addItem(imagesPack);
        addItem(animationsPack);
        addItem(shaderManager);
        addSeparator();
        addItem(placeholders);
        addItem(noise);
        addItem(importSpriteSheet);
    }

    @Override
    public void setProjectOpen(boolean open) {
        for (MenuItem menuItem : new Array.ArrayIterator<>(itemsList))
            menuItem.setDisabled(!open);
    }
}
