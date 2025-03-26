# 🍰 KREAMIFY
한정판 거래 플랫폼, "KREAM" 클론 코딩 백엔드 개발 저장소입니다.
## 👋🏻 Introduction
### 🧑🏻‍💻 Developers 



|                                                         BE                                                         |                                                               BE                                                               |
|:------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------:|
| [<img src="https://avatars.githubusercontent.com/bum0w0" width="100px;" alt="bum0w0"/>](https://github.com/bum0w0) | [<img src="https://avatars.githubusercontent.com/sanchaehwa" width="100px;" alt="sanchaehwa"/>](https://github.com/sanchaehwa) |
|                                                      **김진범**                                                       |                                                            **양화영**                                                             |

### 💡 Project Purpose
실제 운영되고 있는 서비스 기능들을 분석하고 구현함으로써, 시스템 도메인에 대한 이해를 돕고자 합니다.

###  📁 Development Environment

| IDE             | IntelliJ IDEA    |
|-----------------|------------------|
| **Language**    | Java 17          |
| **Framework**   | SpringBoot 3.4.1 |
| **Build Tools** | Gradle 8.12      |
| **DataBase**    | MariaDB 11.6.2   |

### ⚒️ Project Tech Stack

| **Category**  | **Technologies**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |  
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|  
| **Backend**  | ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white)  ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white) ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=white) <br/>![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white) ![Amazon EC2](https://img.shields.io/badge/Amazon%20EC2-FF9900?style=flat-square&logo=Amazon%20EC2&logoColor=white) ![Amazon ECR](https://img.shields.io/badge/Amazon%20ECR-232F3E?style=flat-square&logo=amazonwebservices&logoColor=white) ![Amazon S3](https://img.shields.io/badge/Amazon%20S3-569A31?style=flat-square&logo=Amazon%20S3&logoColor=white) |
### Architecture
![arch](https://github.com/user-attachments/assets/18d85049-8484-4978-b5d2-4144bbb7f6e1)
### ERD
![erd](https://github.com/user-attachments/assets/a834b30e-a06d-4702-8051-6dae221e22f2)

 
## 👥  Cooperation

### 🎋 Branch Convention
#### Branch Structure 
- **main**: 배포용 브랜치 (항상 안정적인 상태 유지)
- **develop**: 통합 개발 브랜치 (다음 배포를 준비)
- **feature/**: 기능 개발 및 이슈 해결 브랜치 (작업 단위)
#### Branch Flow
 ```
Main Branch
  ▲
  └── Develop Branch ── 테스트 완료 후 병합 
                              ▲
                              └── Feature Branch ── 작업 완료 후 병합 
                                          └── 새로운 기능 추가

 ```
