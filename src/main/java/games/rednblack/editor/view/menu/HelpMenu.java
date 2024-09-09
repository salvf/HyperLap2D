package games.rednblack.editor.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import games.rednblack.editor.event.MenuItemListener;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.editor.utils.KeyBindingsLayout;
import games.rednblack.h2d.common.MsgAPI;

import static games.rednblack.h2d.common.MenuAPI.HELP_MENU;

public class HelpMenu extends H2DMenu {

    public static final String ABOUT_DIALOG_OPEN = HyperLap2DMenuBar.prefix + ".ABOUT_DIALOG_OPEN";

    public HelpMenu() {
        super(SettingsManager.translationVO.menu.HELP);
        MenuItem docs = new MenuItem(SettingsManager.translationVO.menu.DOCUMENTATION, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.net.openURI("https://hyperlap2d.rednblack.games/wiki/");
            }
        });
        addItem(docs);

        MenuItem console = new MenuItem(SettingsManager.translationVO.menu.CONSOLE, new MenuItemListener(MsgAPI.OPEN_CONSOLE, null, HELP_MENU))
                .setShortcut(KeyBindingsLayout.getShortcutList(KeyBindingsLayout.OPEN_CONSOLE));
        addItem(console);
        addSeparator();

        MenuItem about = new MenuItem(SettingsManager.translationVO.menu.ABOUT, new MenuItemListener(ABOUT_DIALOG_OPEN, null, HELP_MENU));
        addItem(about);
    }

    @Override
    public void setProjectOpen(boolean open) {

    }
}
