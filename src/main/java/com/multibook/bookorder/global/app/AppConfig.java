package com.multibook.bookorder.global.app;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private static String activeProfile;

    @Getter
    private static String siteName;

    @Getter
    private static EntityManager entityManager;

    @Getter
    private static String tempDirPath;

    @Getter
    private static String genFileDirPath;

    @Getter
    private static String tossPaymentsWidgetSecretKey;

    @Value("${spring.profiles.active}")
    public void setActiveProfile(String value){
        activeProfile = value;
    }

    public static boolean isProd(){
        return activeProfile.equals("prod");
    }

    public static boolean isNotProd(){
        return isProd() == false;
    }

    @Value("${custom.site.name}")
    public void setSiteName(String siteName){
        this.siteName = siteName;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    @Value("${custom.temp.dirPath}")
    public void setTempDirPath(String tempDirPath){
        this.tempDirPath=tempDirPath;
    }

    @Value("${custom.genFile.dirPath}")
    public void setGenFileDirPath(String genFileDirPath){
        this.genFileDirPath  = genFileDirPath;
    }

    @Value("${custom.tossPayments.widget.secretKey}")
    public void setTossPaymentsWidgetSecretKey(String tossPaymentsWidgetSecretKey){
        this.tossPaymentsWidgetSecretKey = tossPaymentsWidgetSecretKey;
    }
}
