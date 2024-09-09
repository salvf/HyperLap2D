package games.rednblack.editor.view.menu;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import games.rednblack.editor.data.manager.PreferencesManager;
import games.rednblack.editor.event.MenuItemListener;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.editor.renderer.utils.HyperJson;
import games.rednblack.editor.utils.KeyBindingsLayout;
import games.rednblack.h2d.common.view.ui.widget.H2DPopupMenu;
import games.rednblack.h2d.common.vo.ProjectVO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

import static games.rednblack.h2d.common.MenuAPI.FILE_MENU;

public class FileMenu extends H2DMenu {

    public static final String NEW_PROJECT = HyperLap2DMenuBar.prefix + ".NEW_PROJECT";
    public static final String OPEN_PROJECT = HyperLap2DMenuBar.prefix + ".OPEN_PROJECT";
    public static final String SAVE_PROJECT = HyperLap2DMenuBar.prefix + ".SAVE_PROJECT";
    public static final String SAVE_PROJECT_AS = HyperLap2DMenuBar.prefix + ".SAVE_PROJECT_AS";
    public static final String RECENT_PROJECTS = HyperLap2DMenuBar.prefix + ".RECENT_PROJECTS";
    public static final String CLEAR_RECENT = HyperLap2DMenuBar.prefix + ".CLEAR_RECENT";
    public static final String EXPORT = HyperLap2DMenuBar.prefix + ".EXPORT";
    public static final String SETTINGS = HyperLap2DMenuBar.prefix + ".SETTINGS";
    public static final String EXIT = HyperLap2DMenuBar.prefix + ".EXIT";

    private final MenuItem saveProject, export, saveProjectAs;

    private final PopupMenu recentProjectsPopupMenu;
    private final Array<MenuItem> recentProjectsMenuItems;

    public FileMenu() {
        super(SettingsManager.translationVO.menu.FILE); //⌘⇧⌥
        saveProject = new MenuItem(SettingsManager.translationVO.menu.SAVEPROJECT, new MenuItemListener(SAVE_PROJECT, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.SAVE_PROJECT));
        saveProjectAs = new MenuItem(SettingsManager.translationVO.menu.SAVEPROJECTAS, new MenuItemListener(SAVE_PROJECT_AS, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.SAVE_PROJECT_AS));
        addItem(new MenuItem(SettingsManager.translationVO.menu.NEWPROJECT, new MenuItemListener(NEW_PROJECT, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.NEW_PROJECT)));
        addItem(new MenuItem(SettingsManager.translationVO.menu.OPENPROJECT, new MenuItemListener(OPEN_PROJECT, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.OPEN_PROJECT)));
        addItem(saveProject);
        addItem(saveProjectAs);
        //
        addSeparator();

        export = new MenuItem(SettingsManager.translationVO.menu.EXPORTPROJECTS, new MenuItemListener(EXPORT, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.EXPORT_PROJECT));
        addItem(export);
        addItem(new MenuItem(SettingsManager.translationVO.menu.SETTINGS, new MenuItemListener(SETTINGS, null, FILE_MENU)).setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.OPEN_SETTINGS)));
        //
        addSeparator();
        MenuItem recentProjectsMenuItem = new MenuItem(SettingsManager.translationVO.menu.RECENTPROJECTS);
        recentProjectsPopupMenu = new H2DPopupMenu();
        recentProjectsMenuItem.setSubMenu(recentProjectsPopupMenu);
        recentProjectsMenuItems = new Array<>();
        addItem(recentProjectsMenuItem);

        PreferencesManager prefs = PreferencesManager.getInstance();
        prefs.buildRecentHistory();
        reInitRecent(prefs.getRecentHistory());
        //
        addSeparator();
        addItem(new MenuItem(SettingsManager.translationVO.menu.EXIT, new MenuItemListener(EXIT, null , FILE_MENU)));
    }

    public String getFolderNameAndPath(String path) {
        String title = "";
        try {
            String projectContents = FileUtils.readFileToString(new File(path), "utf-8");
            Json json = HyperJson.getJson();
            json.setIgnoreUnknownFields(true);
            ProjectVO vo = json.fromJson(ProjectVO.class, projectContents);
            title = vo.projectName + " - [ " + path + "]";
        } catch (Exception e) {
            File path1 = new File(path);
            File path2 = new File(path1.getParent());
            title = path2.getName() + " - [ " + path + "]";
        }

        return title;
    }

    public void addRecent(ArrayList<String> paths) {
        for (String path : paths) {
            MenuItem menuItem = new MenuItem(getFolderNameAndPath(path) , new MenuItemListener(RECENT_PROJECTS, path, FILE_MENU));
            recentProjectsMenuItems.add(menuItem);
            recentProjectsPopupMenu.addItem(menuItem);
        }
    }

    public void reInitRecent(ArrayList<String> paths) {
        if (recentProjectsMenuItems == null || recentProjectsPopupMenu == null)
            return;

        if (recentProjectsMenuItems.size != 0) {
            recentProjectsMenuItems.clear();
        }

        if (recentProjectsPopupMenu.hasChildren()) {
            recentProjectsPopupMenu.remove();
            recentProjectsPopupMenu.clearChildren();
        }

        addRecent(paths);
        if (paths.size() > 0) {
            recentProjectsPopupMenu.addSeparator();
        }

        MenuItem menuItem = new MenuItem(SettingsManager.translationVO.menu.CLEARHISTORY, new MenuItemListener(CLEAR_RECENT, null, FILE_MENU));
        recentProjectsMenuItems.add(menuItem);
        recentProjectsPopupMenu.addItem(menuItem);

        remove();
    }

    public void setProjectOpen(boolean open) {
        saveProjectAs.setDisabled(!open);
        saveProject.setDisabled(!open);
        export.setDisabled(!open);
    }
}
