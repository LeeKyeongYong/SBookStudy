package com.multibook.bookorder.global.initData;

import com.multibook.bookorder.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
@Configuration
@RequiredArgsConstructor
public class All {
    @Autowired
    @Lazy
    private All self;

    @Bean
    @Order(1)
    ApplicationRunner initAll(){
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1(){
        new File(AppConfig.getTempDirPath()).mkdirs();
    }
}
