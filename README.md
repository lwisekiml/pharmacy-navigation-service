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


