# 8. Deployment Diagram

## 8.1 Deployment Diagram

```mermaid
graph TD
    subgraph Client Node
        Browser[Web Browser / User Device]
    end

    subgraph Application Server Node
        subgraph Docker Environment
            AppContainer[Spring Boot App Container<br/>Port 8080]
            DBContainer[PostgreSQL Database Container<br/>Port 5432]
        end
    end

    subgraph External Cloud Services
        PaymentGateway[External Payment Gateway Server<br/>HTTPS API]
    end

    Browser -->|HTTPS / REST API| AppContainer
    AppContainer -->|JDBC Connection| DBContainer
    AppContainer -->|HTTPS API Call| PaymentGateway
```
## 8.2 Deployment Description
Deployment Diagram แสดงโครงสร้างการติดตั้งระบบบนสภาพแวดล้อมจริง (Deployment Environment):

Client Node: อุปกรณ์ของผู้ใช้งานที่เชื่อมต่อผ่าน Web Browser ผ่านโปรโตคอล HTTPS

Application Server Node: เซิร์ฟเวอร์หลักที่รันแอปพลิเคชันผ่าน Docker Container ประกอบด้วย:

Spring Boot App Container: ส่วนประมวลผล Backend รันอยู่บน Port 8080

PostgreSQL DB Container: ส่วนจัดการและจัดเก็บข้อมูล รันอยู่บน Port 5432

External Cloud Services: บริการภายนอกที่เชื่อมต่อผ่าน HTTPS REST API เพื่อประมวลผลการชำระเงินออนไลน์