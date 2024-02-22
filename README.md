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


테스트 실행 후 docker ps 명령어로 확인을 해보면 아래와 같이 나와다가 사라진다.

|CONTAINER ID|   IMAGE|                       COMMAND|                   CREATED|         STATUS|         PORTS|                    NAMES|  
|---|---|---|---|---|---|---|  
|9521566ce5b5|   mariadb:10|                  "docker-entrypoint.s…"|  4 seconds ago|   Up 4 seconds|   0.0.0.0:2544->3306/tcp|   sweet_archimedes|                                         
|bb5130c66326|   redis:6|                     "docker-entrypoint.s…"|   6 seconds ago|   Up 5 seconds|   0.0.0.0:2542->6379/tcp|   modest_mirzakhani|                                        
|458d19266813|   testcontainers/ryuk:0.3.3|   "/app"|                    7 seconds ago|   Up 6 seconds|   0.0.0.0:2540->8080/tcp|   testcontainers-ryuk-32e2ccd1-db82-417b-8e1e-4b42d930c8bb|  

- testcontainers/ryuk:0.3.3 : 테스트가 끝난 후 정리를 해주는 역할을 하는 컨테이너

<br/>

### Ch05-11.약국 데이터 셋업

    > docker-compose -f docker-compose-local.yml up

    # 다른 창에서 아래 실행
    > docker ps
    CONTAINER ID   IMAGE                                      COMMAND                   CREATED         STATUS         PORTS                    NAMES
    1a97ad2840e3   wisekim/pharmacy-recommendation-database   "docker-entrypoint.s…"   2 minutes ago   Up 2 minutes   0.0.0.0:3307->3306/tcp   pharmacy-recommendation-database
    4516156d28f8   wisekim/pharmacy-recommendation-redis      "docker-entrypoint.s…"   30 hours ago    Up 2 minutes   0.0.0.0:6379->6379/tcp   pharmacy-recommendation-redis   

    > docker exec -it 1a97ad2840e3 bash
    > mysql -uroot -p
    # 패스워드 입력
    MariaDB [(none)]>  show databases;
    +-------------------------+
    | Database                |
    +-------------------------+
    | information_schema      |
    | mysql                   |
    | performance_schema      |
    | pharmacy-recommendation |
    | sys                     |
    +-------------------------+
    5 rows in set (0.000 sec)
    
    MariaDB [(none)]> use pharmacy-recommendation
    Reading table information for completion of table and column names
    You can turn off this feature to get a quicker startup with -A
    
    Database changed
    MariaDB [pharmacy-recommendation]> show tables;
    +-----------------------------------+
    | Tables_in_pharmacy-recommendation |
    +-----------------------------------+
    | direction                         |
    | pharmacy                          |
    +-----------------------------------+
    2 rows in set (0.000 sec)

    MariaDB [pharmacy-recommendation]> select * from pharmacy;

<br/>

* 여기서 show tables; 를 하였는데 테이블이 안 나오는 경우
1. 아래 처럼 build 옵션을 추가하게 되면, 기존에 사용했던 이미지를 재사용하여 도커 컨테이너를 띄우지 않고,
   무조건 재빌드 하여 컨테이너를 실행합니다.
> $ docker-compose -f docker-compose-local.yml up --build

또한, 아래 명령어는 현재 사용하지 않는 리소스를 정리하는데 유용한 명령어 이니 참고하면 좋을 것 같습니다.
- 멈춰있는 컨테이너 제거
- 컨테이너에서 사용되지 않는 네트워크 제거
- 불필요한 이미지 및 빌드 캐시 제거
> $ docker system prune

<br/>

2. docker 컨테이너 삭제 후 다시 실행
> docker rm [컨테이너id]  
> 로 삭제 가능하다. 또는 docker desktop 에서 삭제 해도 된다. 그 후에  
> docker-compose -f docker-compose-local.yml up  
> 명령어 부터 다시 실행

<br/>

### Ch07-03.테스트 코드 작성

>./gradlew build -P KAKAO_REST_API_KEY={카카오 api 키값}

<br/>

### Ch08-01.Redis 소개

    $ docker exec -it {Container id} redis-cli --raw // redis cli로 접속
    $ set id 10     // key(id) 의 value를 10으로 저장
    $ get id        // key 조회
    $ del id        // key 삭제
    $ scan 0        // key 들을 일정 단위 개수 만큼씩 조회
    $ hgetall USER  // Key(USER)의 매핑되는 모든 필드과 값들을 조회
    $ hset USER subkey value    // Key(USER)의 subKey 의 값을 지정
    $ hget USER subkey          // Key(USER)의 subKey 의 값을 조회
    
    // geopoints1 라는 자료구조에 pharmacy1, 2 각각 경도, 위도를 추가
    $ geoadd geopoints1 127.0817 37.5505 pharmacy1
    $ geoadd geopoints1 127.0766 37.541 pharmacy2
    // 두 지역의 거리를 리턴한다. 단위는 km
    $ geodist geopoints1 pharmacy1 pharmacy2 km

    > geoadd geopoints1 127.0817 37.5505 pharmacy1
    > geoadd geopoints1 127.0766 37.541 pharmacy2
    > geodist geopoints1 pharmacy1 pharmacy2 km
    1.1483
    
    $ geoadd geopoints2 127.0569046 37.61040424 pharmacy1
    $ geoadd geopoints2 127.029052 37.60894036 pharmacy2
    $ geoadd geopoints2 127.236707811313 37.3825107393401 pharmacy3
    // geopoints2 이름의 자료구조에서 주어진 위도, 경도 기준으로 반경 10km 이내에 가까운 약국 찾기
    $ georadius geopoints2 127.037033003036 37.596065045809 10 km withdist withcoord asc count 3

    > geoadd geopoints2 127.0569046 37.61040424 pharmacy1
    > geoadd geopoints2 127.029052 37.60894036 pharmacy2
    > geoadd geopoints2 127.236707811313 37.3825107393401 pharmacy3
    > georadius geopoints2 127.037033003036 37.596065045809 10 km withdist withcoord asc count 3
    pharmacy2
    1.5953
    127.02905148267745972
    37.60893914265806615
    pharmacy1
    2.3685
    127.05690354108810425
    37.61040421148816648

### Ch08-04.Redis 테스트 코드 작성

    # cmd 창
    > docker-compose -f docker-compose-local.yml up

    # 인텔리제이에서 스프링 시작

    # chrome 에서
    > http://localhost:8080/redis/save 접속 후 로그를 보면 아래와 같은 로그 확인이 가능하다.

    [PharmacyRedisTemplateService save success] id: 1
    [PharmacyRedisTemplateService save success] id: 2
    [PharmacyRedisTemplateService save success] id: 3
    ...

    # cmd 창
    > docker ps
    > docker exec -it {redis CONTAINER ID} redis-cli --raw  
    > hgetall PHARMACY
    130
    {"id":130,"pharmacyName":"백화점약국","pharmacyAddress":"서울특별시 성북구  돌곶이로  61 (석관동)","latitude":37.60916369,"longitude":127.0585119}
    139
    {"id":139,"pharmacyName":"삼원약국","pharmacyAddress":"서울특별시 성북구 종암로 120 (종암동","latitude":37.60102101,"longitude":127.0344393}
    ...
    
    # 130 은 서브키 값(id값/db sequence 값)
    # {...}은 JSON

    # http://localhost:8080/ 접속하여 검색후 로그 창을 보면
    : redis findAll success!
    가 찍혀 있는것을 확인할 수 있다.

<참고>
> docker-compose -f docker-compose-local.yml down  
> 전체 컨테이너 제거하고 리소스 정리

