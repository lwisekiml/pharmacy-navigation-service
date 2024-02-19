# pharmacy-navigation-service

<br/>

### Ch04-03. 도커를 이용한 싱글컨테이너 어플리케이션 실습

    > docker -v  
    > docker-compose -v  
    > docker login
    > ./gradlew build
    
    # build/libs에 2개의 jar이 생긴다.
    # plain이 있는 것 : 모든 의존성을 포함하지 않는 jar
    # plain이 없는 것 : 모든 의존성을 포함하는 jar
    # build.gradle에 아래 내용 추가
    #
    #    bootJar {
    #    archiveFileName = 'app.jar'
    #    }
    #
    
    > ./gradlew clean
    > ./gradlew build
    
    # Dockerfile 생성
    
    > java -jar build/libs/app.jar
    # 스프링 실행을 확인할 수 있다.
    
    > docker build -t [도커허브ID]/application-project-test .
    > docker images
    > docker run [도커허브ID]/application-project-test -p 8080:8080
    #                                           -p 호스트 포트:도커 컨테이너 포트
    # 스프링 실행을 확인할 수 있다.                 
    # docker 컨테이너가 독립되 공간에서 실행된다.
    # 그새서 호스트와 도커 컨테이너 간에 포트 포워드를 해줘야 한다.(-p 8080:8080)
    
    > docker exec -it [CONTAINER ID] bash
    # 컨테이너를 sh, bash 등의 터미널 환경으로 접근

<br/>

### Ch04-06.도커를 이용한 다중 컨테이너 환경 구현하기
    > docker-compose -f docker-compose-local.yml up  
    
    # 다른 창에서 아래 명령어를 실행하여 database와 redis 실행확인

    > docker ps
    CONTAINER ID   IMAGE                                      COMMAND                   CREATED          STATUS          PORTS                    NAMES
    63a99c729e25   wisekim/pharmacy-recommendation-database   "docker-entrypoint.s…"   47 seconds ago   Up 39 seconds   0.0.0.0:3307->3306/tcp   pharmacy-recommendation-database
    4516156d28f8   wisekim/pharmacy-recommendation-redis      "docker-entrypoint.s…"   47 seconds ago   Up 39 seconds   0.0.0.0:6379->6379/tcp   pharmacy-recommendation-redis

    application.yml에 있는 아래 값은 Edit Configurations... 
    -> Modify options -> Environment variables 선택
    -> Environment variables 오른쪽 $ 클릭
    -> '+'를 눌러 Name에 SPRING_DATASOURCE_USERNAME을 쓰고 Value에 값을 쓰면 된다.
       SPRING_DATASOURCE_PASSWORD 도 똑같이 해주면 된다.

    # ProjectApplication 실행하여 서버가 잘 뜨는지 확인

.env 파일 : docker-compose가 참조하는 파일

<br/>

### Ch05-02. kakao 주소검색 api 구현하기

    application.yml에 있는 KAKAO_REST_API_KEY는 Edit Configurations... 에 등록한다.

<br/>

### Ch05-04. Testcontainers 소개
- TestContainers는 Java 언어만으로 docker container를 활용한 테스트 환경 구성  
- 도커를 이용하여 테스트할 때 컨테이너를 직접 관리해야 하는 번거로움을 해결 해주며, 운영환경과 유사한 스펙으로 테스트 가능
- 즉, 테스트 코드가 실행 될 때 자동으로 도커 컨테이너를 실행하여 테스트 하고, 테스트가 끝나면 자동으로 컨테이너를 종료 및 정리

<br/>

### Ch05-05. Testcontainers를 이용한 통합 테스트 환경 구성

    test/resources/application.yml에 있는 KAKAO_REST_API_KEY값은 application.yml에 있는 KAKAO_REST_API_KEY는 Edit Configurations... 에 등록한다.

<br/>

###
테스트 실행 후 docker ps 명령어로 확인을 해보면 아래와 같이 나와다가 사라진다.

CONTAINER ID   IMAGE                       COMMAND                   CREATED         STATUS         PORTS                    NAMES                                                   
9521566ce5b5   mariadb:10                  "docker-entrypoint.s…"   4 seconds ago   Up 4 seconds   0.0.0.0:2544->3306/tcp   sweet_archimedes                                         
bb5130c66326   redis:6                     "docker-entrypoint.s…"   6 seconds ago   Up 5 seconds   0.0.0.0:2542->6379/tcp   modest_mirzakhani                                        
458d19266813   testcontainers/ryuk:0.3.3   "/app"                    7 seconds ago   Up 6 seconds   0.0.0.0:2540->8080/tcp   testcontainers-ryuk-32e2ccd1-db82-417b-8e1e-4b42d930c8bb

testcontainers/ryuk:0.3.3 : 테스트가 끝난 후 정리를 해주는 역할을 하는 컨테이너

