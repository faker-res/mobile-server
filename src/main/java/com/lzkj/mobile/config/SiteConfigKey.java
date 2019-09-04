package com.lzkj.mobile.config;

public enum SiteConfigKey {
    ContactConfig("ContactConfig"),
    SiteConfig("SiteConfig"),
    GameFullPackageConfig("GameFullPackageConfig"),
    GameJanePackageConfig("GameJanePackageConfig"),
    GameAndroidConfig("GameAndroidConfig"),
    GameIosConfig("GameIosConfig"),
    GameWin32Config("GameWin32Config"),
    MobilePlatformVersion("MobilePlatformVersion"),
    DayTaskConfig("DayTaskConfig"),
    ServerConfig("ServerConfig");
    private String name;
    SiteConfigKey(String name){
     this.name = name;
    }
    public String getName() {
        return name;
    }
}
