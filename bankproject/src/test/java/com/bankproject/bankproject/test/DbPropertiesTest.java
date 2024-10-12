package com.bankproject.bankproject.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@org.springframework.boot.test.context.SpringBootTest
@ConfigurationProperties
public class DbPropertiesTest {

    /*
     * application-secret.properties 에 내용을 작성해 놓고 확인용으로 사용
     */

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    @Value("${custom.fileTempDirPath}")
    private String fileTempDirPath;

    @Test
    public void test() {
        log.info("url: " + url);
        log.info("username: " + username);
        log.info("password: " + password);
        log.info("fileDirPath: " + fileDirPath);
        log.info("fileTempDirPath: " + fileTempDirPath);

        Assert.notNull(url, "url is null");
        Assert.notNull(username, "username is null");
        Assert.notNull(password, "password is null");
        Assert.notNull(fileDirPath, "fileDirPath is null");
        Assert.notNull(fileTempDirPath, "fileTempDirPath is null");
    }
}
