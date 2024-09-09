package games.rednblack.editor.view.ui.settings;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import games.rednblack.editor.proxy.SettingsManager;
import games.rednblack.editor.utils.RoundUtils;
import games.rednblack.h2d.common.MsgAPI;
import games.rednblack.h2d.common.view.SettingsNodeValue;
import games.rednblack.h2d.common.view.ui.StandardWidgetsFactory;
import games.rednblack.h2d.common.vo.EditorConfigVO;
import games.rednblack.puremvc.Facade;

public class GeneralSettings extends SettingsNodeValue<EditorConfigVO> {

    private final VisCheckBox autoSaving, useANGLEGLES2, failSafeException;
    private final VisCheckBox enablePlugins;
    private VisSelectBox<String> filterKeyMapping, tranlationsfiles;
    private VisSlider uiScaleDensity, msaaSamples, fpsLimit;

    public GeneralSettings() {
        super("General", Facade.getInstance());

        getContentTable().add("Editor").left().row();
        getContentTable().addSeparator();
        autoSaving = StandardWidgetsFactory.createCheckBox("Save changes automatically [EXPERIMENTAL]");
        getContentTable().add(autoSaving).left().padTop(5).padLeft(8).row();

        failSafeException = StandardWidgetsFactory.createCheckBox("Keep alive on exceptions [EXPERIMENTAL]");
        getContentTable().add(failSafeException).left().padTop(5).padLeft(8).row();

        getContentTable().add(getKeyMappingTable()).left().padTop(5).row();

        getContentTable().add(getTranslationsTable()).left().padTop(5).row();

        getContentTable().add(getUiScaleDensityTable()).left().padTop(5).row();

        getContentTable().add("Plugins").left().padTop(10).row();
        getContentTable().addSeparator();
        enablePlugins = StandardWidgetsFactory.createCheckBox("Enable plugins");
        getContentTable().add(enablePlugins).left().padTop(5).padLeft(8).row();

        getContentTable().add("Performance").left().padTop(10).row();
        getContentTable().addSeparator();
        getContentTable().add(getMassSamplesTable()).left().padTop(5).row();
        getContentTable().add(getFPSLimitTable()).left().padTop(5).row();

        useANGLEGLES2 = StandardWidgetsFactory.createCheckBox("Use ANGLE OpenGL ES 2 API");
        getContentTable().add(useANGLEGLES2).left().padTop(5).padLeft(8).row();
    }

    private Actor getKeyMappingTable() {
        SettingsManager settingsManager = facade.retrieveProxy(SettingsManager.NAME);

        VisTable mappingTable = new VisTable();
        mappingTable.add("Key mapping:").padLeft(8);
        filterKeyMapping = StandardWidgetsFactory.createSelectBox(String.class);
        filterKeyMapping.setItems(settingsManager.getKeyMappingFiles());
        mappingTable.add(filterKeyMapping).padLeft(8);

        return mappingTable;
    }

    private Actor getTranslationsTable() {
        SettingsManager settingsManager = facade.retrieveProxy(SettingsManager.NAME);

        VisTable translationsTable = new VisTable();
        translationsTable.add("Translations:").padLeft(8);
        tranlationsfiles = StandardWidgetsFactory.createSelectBox(String.class);
        tranlationsfiles.setItems(settingsManager.getTranslationsFiles());
        translationsTable.add(tranlationsfiles).padLeft(8);

        return translationsTable;
    }

    private Actor getUiScaleDensityTable() {
        VisTable scaleTable = new VisTable();

        scaleTable.add("UI Scale Density:").padLeft(8);
        uiScaleDensity = StandardWidgetsFactory.createSlider(0.5f, 1.5f, 0.1f);
        scaleTable.add(uiScaleDensity).padLeft(8);
        VisLabel labelFactor = StandardWidgetsFactory.createLabel("", "default", Align.left);
        scaleTable.add(labelFactor).padLeft(8);
        labelFactor.setText(getUIScaleDensity() + "x");
        uiScaleDensity.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                labelFactor.setText(getUIScaleDensity() + "x");
            }
        });

        return scaleTable;
    }

    private Actor getMassSamplesTable() {
        VisTable msaaTable = new VisTable();

        msaaTable.add("MSAA Samples:").padLeft(8);
        msaaSamples = StandardWidgetsFactory.createSlider(0, 16, 1);
        msaaTable.add(msaaSamples).padLeft(8);
        VisLabel labelFactor = StandardWidgetsFactory.createLabel("", "default", Align.left);
        msaaTable.add(labelFactor).padLeft(8);
        labelFactor.setText(getMsaaSamples());
        msaaSamples.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                labelFactor.setText(getMsaaSamples());
            }
        });

        return msaaTable;
    }

    private Actor getFPSLimitTable() {
        VisTable fpsLimitTable = new VisTable();

        fpsLimitTable.add("FPS Limit:").padLeft(8);
        fpsLimit = StandardWidgetsFactory.createSlider(0, 240, 10);
        fpsLimitTable.add(fpsLimit).padLeft(8);
        VisLabel labelFactor = StandardWidgetsFactory.createLabel("", "default", Align.left);
        fpsLimitTable.add(labelFactor).padLeft(8);
        labelFactor.setText("Unlimited");
        fpsLimit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (getFPSLimit() == 0)
                    labelFactor.setText("Unlimited");
                else
                    labelFactor.setText(getFPSLimit());
            }
        });

        return fpsLimitTable;
    }

    private float getUIScaleDensity() {
        return RoundUtils.round(uiScaleDensity.getValue(), 2);
    }

    private int getMsaaSamples() {
        return (int) msaaSamples.getValue();
    }

    private int getFPSLimit() {
        return (int) fpsLimit.getValue();
    }

    private String getLang() {
        return tranlationsfiles.getSelected();
    }

    @Override
    public void translateSettingsToView() {
        autoSaving.setChecked(getSettings().autoSave);
        useANGLEGLES2.setChecked(getSettings().useANGLEGLES2);
        failSafeException.setChecked(getSettings().failSafeException);
        enablePlugins.setChecked(getSettings().enablePlugins);
        filterKeyMapping.setSelected(getSettings().keyBindingLayout);
        uiScaleDensity.setValue(getSettings().uiScaleDensity);
        msaaSamples.setValue(getSettings().msaaSamples);
        fpsLimit.setValue(getSettings().fpsLimit);
        tranlationsfiles.setSelected(getSettings().lang);
    }

    @Override
    public void translateViewToSettings() {
        getSettings().autoSave = autoSaving.isChecked();
        getSettings().useANGLEGLES2 = useANGLEGLES2.isChecked();
        getSettings().failSafeException = failSafeException.isChecked();
        getSettings().enablePlugins = enablePlugins.isChecked();
        getSettings().keyBindingLayout = filterKeyMapping.getSelected();
        getSettings().uiScaleDensity = getUIScaleDensity();
        getSettings().msaaSamples = getMsaaSamples();
        getSettings().fpsLimit = getFPSLimit();
        getSettings().lang = getLang();
        facade.sendNotification(MsgAPI.SAVE_EDITOR_CONFIG);
    }

    @Override
    public boolean validateSettings() {
        return getSettings().autoSave != autoSaving.isChecked()
                || getSettings().useANGLEGLES2 != useANGLEGLES2.isChecked()
                || getSettings().failSafeException != failSafeException.isChecked()
                || getSettings().enablePlugins != enablePlugins.isChecked()
                || !getSettings().keyBindingLayout.equals(filterKeyMapping.getSelected())
                || getSettings().uiScaleDensity != getUIScaleDensity()
                || getSettings().msaaSamples != getMsaaSamples()
                || getSettings().fpsLimit != getFPSLimit()
                || getSettings().lang != getLang();
    }

    @Override
    public boolean requireRestart() {
        return getSettings().useANGLEGLES2 != useANGLEGLES2.isChecked()
                || getSettings().failSafeException != failSafeException.isChecked()
                || getSettings().enablePlugins != enablePlugins.isChecked()
                || !getSettings().keyBindingLayout.equals(filterKeyMapping.getSelected())
                || getSettings().msaaSamples != getMsaaSamples()
                || getSettings().fpsLimit != getFPSLimit()
                || getSettings().lang != getLang();
    }
}
