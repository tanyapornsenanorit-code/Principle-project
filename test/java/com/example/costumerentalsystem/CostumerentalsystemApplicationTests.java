package com.example.costumerentalsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // ใช้ H2 ไม่ต้องพึ่ง PostgreSQL (ดู test/resources/application-test.properties)
class CostumerentalsystemApplicationTests {

    @Test
    void contextLoads() {
    }
}
