package games.rednblack.editor.proxy;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import games.rednblack.editor.CustomExceptionHandler;
import games.rednblack.editor.Main;
import games.rednblack.editor.renderer.utils.HyperJson;
import games.rednblack.editor.utils.HyperLap2DUtils;
import games.rednblack.editor.utils.KeyBindingsLayout;
import games.rednblack.h2d.common.vo.EditorConfigVO;
import games.rednblack.h2d.common.vo.TranslationVO;
import games.rednblack.puremvc.Proxy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.List;

/**
 * Manages the settings and configurations for the HyperLap2D editor.
 * Handles initialization of workspace, loading and saving of editor configurations,
 * and management of translation files.
 */
public class SettingsManager extends Proxy {
    private static final String TAG = SettingsManager.class.getCanonicalName();
    public static final String NAME = TAG;

    private String DEFAULT_FOLDER = "HyperLap2D";

    private String defaultWorkspacePath;
    public EditorConfigVO editorConfigVO;
    public static TranslationVO translationVO;

    public File[] pluginDirs;
    public File cacheDir;

    /**
     * Constructor for SettingsManager.
     * Initializes the workspace and sets up necessary directories.
     */
    public SettingsManager() {
        super(NAME, null);
        initWorkspace();
    }

    @Override
    public void onRegister() {
        super.onRegister();
        KeyBindingsLayout.init();
    }

    /**
     * Initializes the workspace by setting up directories and loading configurations.
     */
    private void initWorkspace() {
        try {
            editorConfigVO = getEditorConfig();
            String myDocPath = HyperLap2DUtils.MY_DOCUMENTS_PATH;
            defaultWorkspacePath = myDocPath + File.separator + DEFAULT_FOLDER;
            FileUtils.forceMkdir(new File(defaultWorkspacePath));
            FileUtils.forceMkdir(new File(HyperLap2DUtils.getKeyMapPath()));

            pluginDirs = new File[]{new File(Main.getJarContainingFolder(Main.class) + File.separator + "plugins"),
                    new File(HyperLap2DUtils.getRootPath() + File.separator + "plugins"),
                    new File(System.getProperty("user.dir") + File.separator + "plugins")};
            cacheDir = new File(HyperLap2DUtils.getRootPath() + File.separator + "cache");
            FileUtils.forceMkdir(cacheDir);
            initLang();
        } catch (IOException e) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            e.printStackTrace();
            String stacktrace = result.toString();
            CustomExceptionHandler.showErrorDialog(null, stacktrace);
        }
    }

    /**
     * Initializes the language settings by loading the appropriate translation file.
     */
    private void initLang(){
        File langFile = new File(HyperLap2DUtils.getTranslationsPath()+ File.separator+editorConfigVO.lang+".json");
        if (!langFile.isDirectory()) {
            String translationContents = null;
            try {
                translationContents = FileUtils.readFileToString(langFile, "utf-8");
                Json json = HyperJson.getJson();
                json.setIgnoreUnknownFields(true);
                translationVO = json.fromJson(TranslationVO.class, translationContents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the workspace path.
     * @return the workspace path as a FileHandle.
     */
    public FileHandle getWorkspacePath() {
        if (!editorConfigVO.lastOpenedSystemPath.isEmpty()) {
            return new FileHandle(editorConfigVO.lastOpenedSystemPath);
        }
        return new FileHandle(defaultWorkspacePath);
    }

    /**
     * Gets the import path.
     * @return the import path as a FileHandle, or null if not set.
     */
    public FileHandle getImportPath() {
        if (!editorConfigVO.lastImportedSystemPath.isEmpty()) {
            return new FileHandle(editorConfigVO.lastImportedSystemPath);
        }
        return null;
    }

    /**
     * Gets the key mapping files.
     * @return an array of key mapping file names.
     */
    public String[] getKeyMappingFiles() {
        File mappingDir = new File(HyperLap2DUtils.getKeyMapPath());
        String[] extensions = new String[]{"keymap"};
        List<File> files = FileUtils.listFiles(mappingDir, extensions, true).stream().toList();
        String[] maps = new String[files.size() + 1];
        maps[0] = "default";
        for (int i = 0; i < files.size(); i++) {
            maps[i + 1] = FilenameUtils.removeExtension(files.get(i).getName());
        }
        return maps;
    }

    /**
     * Gets the translation files.
     * @return an array of translation file names.
     */
    public String[] getTranslationsFiles() {
        File translationsDir = new File(HyperLap2DUtils.getTranslationsPath());
        String[] extensions = new String[]{"json"};
        List<File> files = FileUtils.listFiles(translationsDir, extensions, false).stream().toList();
        String[] langfiles = new String[files.size() ];
        for (int i = 0; i < files.size(); i++) {
            langfiles[i] = FilenameUtils.removeExtension(files.get(i).getName());
        }
        return langfiles;
    }

    /**
     * Gets the editor configuration.
     * @return the editor configuration as an EditorConfigVO object.
     */
    private EditorConfigVO getEditorConfig() {
        EditorConfigVO editorConfig = new EditorConfigVO();
        String configFilePath = HyperLap2DUtils.getRootPath() + File.separator + "configs" + File.separator + EditorConfigVO.EDITOR_CONFIG_FILE;
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            try {
                FileUtils.writeStringToFile(new File(configFilePath), editorConfig.constructJsonString(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Json json = HyperJson.getJson();
            String editorConfigJson;
            try {
                editorConfigJson = FileUtils.readFileToString(configFile, "utf-8");
                editorConfig = json.fromJson(EditorConfigVO.class, editorConfigJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return editorConfig;
    }

    /**
     * Sets the last opened path in the editor configuration.
     * @param path the path to set as the last opened path.
     */
    public void setLastOpenedPath(String path) {
        editorConfigVO.lastOpenedSystemPath = path;
        saveEditorConfig();
    }

    /**
     * Sets the last imported path in the editor configuration.
     * @param path the path to set as the last imported path.
     */
    public void setLastImportedPath(String path) {
        editorConfigVO.lastImportedSystemPath = path;
        saveEditorConfig();
    }

    /**
     * Saves the editor configuration to a file.
     */
    public void saveEditorConfig() {
        try {
            String configFilePath = HyperLap2DUtils.getRootPath() + File.separator + "configs" + File.separator + EditorConfigVO.EDITOR_CONFIG_FILE;
            FileUtils.writeStringToFile(new File(configFilePath), editorConfigVO.constructJsonString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
