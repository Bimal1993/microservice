# 3 commands 
$ docker container ls -a   
$ docker container stop "container-id" 				//stop container.    
$ docker container prune                      // remove all stop images

# How to create networks
$ docker network ls     
$ docker network create  spring-net     

# how to create and run images  
docker build . -t bimal1993/accounts                  
docker run --network spring-net -p 8080:8081 bimal1993/accounts         
docker build . -t bimal1993/loans         
docker run --network spring-net -p 8090:8090 bimal1993/loans      
docker build .  -t bimal1993/card         
docker run --network spring-net -p 9000:9000 bimal1993/card           

# To create mysql local server using docker image
$ docker run --name mysql-standalone --network spring-net -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=dockertest -d mysql

# Properties file 
spring.datasource.url = jdbc:mysql://mysql-standalone:3306/dockertest?useSSL=false          
spring.datasource.username = root                 
spring.datasource.password = 1234                         
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect                 
spring.jpa.hibernate.ddl-auto=update                
spring.jpa.show-sql=true                    
server.port=8081              

# Dockerfile
FROM openjdk:11-slim as build                         
COPY target/*.jar accounts-0.0.1-SNAPSHOT.jar                                 
ENTRYPOINT ["java","-jar","/accounts-0.0.1-SNAPSHOT.jar"]       

# docker-compose.yml    
version: '3.8'                      
services:                       

  mysql-standalone:                       
    image: mysql                        
    container_name: mysql-standalone          
    networks:               
    - spring-net
    environment:
      - MYSQL_ROOT_PASSWORD=1234
      - MYSQL_DATABASE=dockertest

  accounts:                   
    image: bimal1993/accounts:latest          
    restart: on-failure             
    ports:            
    - 8080:8081             
    networks:           
    - spring-net              
    depends_on:               
      - mysql-standalone          
      
  loans:                  
    image: bimal1993/loans:latest             
    restart: on-failure                   
    ports:                      
    - 8090:8090                         
    networks:                 
    - spring-net                    
    depends_on:                   
      - mysql-standalone                
    
  cards:                          
    image: bimal1993/card:latest            
    restart: on-failure               
    ports:                      
    - 9000:9000                     
    networks:                 
    - spring-net            
    depends_on:               
      - mysql-standalone            
  
networks:               
     spring-net:            
        
# After creating docker in mysql to run and check 
$ docker container exec -it  83d68e5298df bash              
mysql -uroot -p               
1234                

# To run docker on root directory           
docker compose up                   
docker compose stop     


# Sample Mysql output
D:\stsworkspace\account>docker container exec -it c8893900c007 bash       
root@c8893900c007:/# mysql -uroot -p      
Enter password:       
ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/var/run/mysqld/mysqld.sock' (2)    
root@c8893900c007:/# mysql -uroot -p      
Enter password:     
ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/var/run/mysqld/mysqld.sock' (2)      
root@c8893900c007:/# mysql -uroot -p    
Enter password:   
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 8
Server version: 8.0.29 MySQL Community Server - GPL

Copyright (c) 2000, 2022, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective       
owners.           

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.        

mysql> show databases;              
+--------------------+    
| Database           |    
+--------------------+  
| dockertest         |    
| information_schema |    
| mysql              |    
| performance_schema |    
| sys                |    
+--------------------+    
5 rows in set (0.02 sec)          

# insert squey 

INSERT INTO `customer` (customer_id,`name`,`email`,`mobile_number`,`create_dt`) VALUES (1,'Jhon','Jhon@gmail.com','9876548337',CURDATE());      

INSERT INTO `account` (account_number,`customer_id`, `account_type`, `branch_address`, `create_dt`)       
VALUES (186576453,1, 'Savings', '123 Main Street, New York', CURDATE());        


INSERT INTO `card` (card_id,`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`, `create_dt`)     
 VALUES (1,'4565XXXX4656', 1, 'Credit', 10000, 500, 9500, CURDATE());

INSERT INTO `card` (card_id,`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`, `create_dt`)
 VALUES (2,'3455XXXX8673', 1, 'Credit', 7500, 600, 6900, CURDATE());      
 
INSERT INTO `card` (card_id,`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`, `create_dt`)     
 VALUES (3,'2359XXXX9346', 1, 'Credit', 20000, 4000, 16000, CURDATE());     

INSERT INTO `loan` (loan_number,`customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)     
 VALUES (1, 1, '2020-10-13', 'Home', 200000, 50000, 150000, '2020-10-13');        
  
INSERT INTO `loan` (loan_number, `customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)        
 VALUES (2, 1, '2020-06-06', 'Vehicle', 40000, 10000, 30000, '2020-06-06');         
    
INSERT INTO `loan` (loan_number, `customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)        
 VALUES (3, 1, '2021-02-14', 'Home', 50000, 10000, 40000, '2018-02-14');        

INSERT INTO `loan` (loan_number,`customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)     
 VALUES (4, 1, '2018-02-14', 'Personal', 10000, 3500, 6500, '2018-02-14');        
  
 
 # http://localhost:8080/myaccount
 Request:
 {  
   "customerId": 1  
 }               
 Response:                
 {  
    "customerId": 1,            
    "accountNumber": 186576453,         
    "accountType": "Savings",         
    "branchAddress": "123 Main Street, New York",       
    "createDt": "2022-07-04"      
 }         
 
# http://localhost:8090/myLoans
Request:          
{ 
    "customerId": 1     
}     
      
Response:                
[            
    {        
        "loanNumber": 3,      
        "customerId": 1,      
        "startDt": "2021-02-14",    
        "loanType": "Home",   
        "totalLoan": 50000,     
        "amountPaid": 10000,      
        "outstandingAmount": 40000,     
        "createDt": "2018-02-14"      
    },
    {       
        "loanNumber": 1,    
        "customerId": 1,    
        "startDt": "2020-10-13",    
        "loanType": "Home",   
        "totalLoan": 200000,  
        "amountPaid": 50000,    
        "outstandingAmount": 150000,    
        "createDt": "2020-10-13"    
    },      
    {       
        "loanNumber": 2,    
        "customerId": 1,    
        "startDt": "2020-06-06",    
        "loanType": "Vehicle",    
        "totalLoan": 40000,     
        "amountPaid": 10000,      
        "outstandingAmount": 30000,     
        "createDt": "2020-06-06"      
    },          
    {         
        "loanNumber": 4,        
        "customerId": 1,    
        "startDt": "2018-02-14",      
        "loanType": "Personal",     
        "totalLoan": 10000,
        "amountPaid": 3500,   
        "outstandingAmount": 6500,    
        "createDt": "2018-02-14"    
    }     
]     

# http://localhost:9000/myCards
Request:      
{   
    "customerId": 1     
}   
Response:          
[       
    {       
        "cardId": 1,        
        "customerId": 1,        
        "cardNumber": "4565XXXX4656",     
        "cardType": "Credit",        
        "totalLimit": 10000,      
        "amountUsed": 500,    
        "availableAmount": 9500,      
        "createDt": "2022-07-04"      
    },      
    {     
        "cardId": 2,      
        "customerId": 1,      
        "cardNumber": "3455XXXX8673",     
        "cardType": "Credit",     
        "totalLimit": 7500,     
        "amountUsed": 600,      
        "availableAmount": 6900,      
        "createDt": "2022-07-04"      
    },      
    {   
        "cardId": 3,      
        "customerId": 1,          
        "cardNumber": "2359XXXX9346",   
        "cardType": "Credit",     
        "totalLimit": 20000,      
        "amountUsed": 4000,     
        "availableAmount": 16000,   
        "createDt": "2022-07-04"      
    }   
]       





  
  

