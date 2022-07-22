package com.gabjuho.QuestMenuPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {
    private final Main plugin; // 메인클래스 객체
    private static ConfigManager instance = new ConfigManager(Main.getPlugin(Main.class)); //데이터 매니저는 싱글톤으로 생성
    private FileConfiguration config = null;
    private File configFile = null;

    private ConfigManager(Main plugin) // 생성자 막아놓음 (싱글톤 패턴으로 인한)
    {
        this.plugin = plugin;
        saveDefaultConfig();
    }
    public static ConfigManager getInstance() // 싱글톤 인스턴스 반환
    {
        if(instance == null)
            instance = new ConfigManager(Main.getPlugin(Main.class));
        return instance;
    }

    public void reloadConfig() // 데이터 리로드
    {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(),"config.yml");

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource("config.yml");
        if(defaultStream != null)
        {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.config.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig()
    {
        if(this.config == null)
            reloadConfig();

        return this.config;
    }

    public void saveConfig() // 데이터 저장
    {
        if(this.config == null || this.configFile == null)
            return;

        try
        {
            this.getConfig().save(this.configFile);
        }catch(IOException e)
        {
            plugin.getLogger().log(Level.SEVERE,"config 파일이 저장되지 않았습니다. -> " + this.configFile,e);
        }
    }

    public void saveDefaultConfig() // 데이터 디폴트 저장
    {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(),"config.yml");

        if(!this.configFile.exists())
        {
            this.plugin.saveResource("config.yml",false);
        }
    }

}
