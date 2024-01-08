package com.ll.lion.user.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class MailPropertiesTest {

    @Autowired
    private MailProperties mailProperties;

    @Test
    @DisplayName("mailProperties에 값이 제대로 들어가는지 확인하는 테스트")
    public void propertiesLoadTest() {
        // 설정 값을 확인
        assertThat(mailProperties.getHost()).isEqualTo("예상되는 호스트 값");
        assertThat(mailProperties.getPort()).isEqualTo("예상되는 포트 값");
        assertThat(mailProperties.getUsername()).isEqualTo("예상되는 사용자 이름");
        assertThat(mailProperties.getPassword()).isEqualTo("예상되는 비밀번호");

        Map<String, String> properties = mailProperties.getProperties();
        assertThat(properties.get("특정 속성 키")).isEqualTo("예상되는 속성 값");
    }
}