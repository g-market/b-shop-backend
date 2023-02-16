# 가비아 B-Shop 쇼핑몰
[![pipeline status](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/badges/develop/pipeline.svg)](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/-/commits/develop)
[![coverage report](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/badges/develop/coverage.svg)](http://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/g-market/gabia_b_shop_backend/-/commits/develop)

## 개발 환경
- **SpringBoot 3.0.2**
- **MySQL 8.0.31**
- **Docker 20.10.23**
- **Nginx**
- **Redis 6.2.7**
- **MinIO**
- **Vue.js 3**
## 프로젝트 세팅
1. `MySql`, `redis` 컨테이너 실행
```bash
docker-compose up -v
```

2. .env 파일 생성
```text
//.env
DB_NAME=<DB_NAME>
DB_PASSWORD=<PASSWORD>
REDIS_PORT=<REDIS_PORT>
DB_PORT=<DB_PORT>
```

3. 환경변수 세팅
```text
ACCESS-EXPIRED-TIME=3600000;
ACCESSTOKEN-URL=<ACCESSTOKEN_URL>;
ACTIVE-PROFILE=local;
CLIENT-ID=<CLIENT_ID>;
CLIENT-SECRET=<CLIENT_SECRET>;
DB-PASSWORD=<DB_PASSWORD>;
DB-URL=jdbc:mysql://localhost:<DB_PORT>/<DB_NAME>;
DB-USERNAME=<DB_USERNAME>;
DDL-AUTO=create;
FETCH-SIZE=<FETCH_SIZE>;
LOG-LEVEL=debug;
REDIS-HOST=<REDIS_HOST>;
REDIS-PORT=<REDIS_PORT>;
REFRESH-EXPIRED-TIME=1209600000;
SERVER-PORT=<SERVER_PORT>;
TOKEN-SECRET=<TOKEN_SECRRT>;
USER-URL=<USER_URL>
```

## 역할 분담
- Backend : @Becker @summer @Jenna @jaime
- Frontend : @Jenna @jaime
- 성능 최적화 : @summer
- 인프라 구성(CI/CD) : @Becker

## 설계문서

- [요구사항 명세서](https://docs.google.com/spreadsheets/d/1D3msOqFlDn56cpEK7gpjG3vZ0YlrxhClL3Rwe2XgfOg/edit#gid=283869447)
- [기능 명세서(Process flowchart)](https://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/gabia_b_shop_backend/-/wikis/%EA%B8%B0%EB%8A%A5%EB%AA%85%EC%84%B8%EC%84%9C(Process-flow-chart))
- [ERD](https://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/gabia_b_shop_backend/-/wikis/ERD)
- [시스템 아키택처 설계](https://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/gabia_b_shop_backend/-/wikis/%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%95%84%ED%82%A4%ED%83%9D%EC%B2%98-%EC%84%A4%EA%B3%84)
- [API documentation](https://documenter.getpostman.com/view/25518655/2s935isRVY#638ca835-f292-4360-afa2-a9d38a5ccd7f)
- [화면 설계](https://mentoring-gitlab.gabia.com/mentee/mentee_2023.01/team/gabia_b_shop_backend/-/wikis/%ED%99%94%EB%A9%B4-Wireframe)

