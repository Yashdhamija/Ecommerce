# Ecommerce

EECS 4413 E-Commerce project - Bookland - is a web e-commerce application. The project supports functionality for the following type of users; visitors, customers, business partners, and administrators. Refer to the access metrix to get a clear understanding of what type of users can perform what kinds of actions https://github.com/yabokar1/Ecommerce/blob/refactored/accessmatrix.jpg.

The project is deployed at - https://qasimahmed.me/BookLand/Home

# Tech Stack
- Front-end: HTML, CSS, JS, Java Server Pages (JSPX)
- Back-end: Java
- Restful-api-Services: Jersey
- Db: MySql, JDBC
- Development: Eclipse Java EE
- Apache Tomcat - local server
- Compiled via maven
- Deployment: Aws EC2

# Report
A design report for the project can be found here - https://github.com/yabokar1/Ecommerce/blob/refactored/4413%20Design%20Document%20(1).pdf

# How to run locally

Clone the repository (refactored- default branch) using 'git clone https://github.com/yabokar1/Ecommerce.git'. You can use the IDE of your choice, and as mentioned the development setup for the project was Eclipse Java EE, Apache tomcat v8.5, JRE 9 or higher (required for jersey), Jersey for Restful-Api service.

To run the project on eclipse, you can right click on Home.java servlet and run it on Tom Cat server and you will see the main page of book store.

(Note - If the cloning is done using eclipse by using clone URI option then there might be errors in the project once the project is successfully imported. Simply right click on project > Maven > Update Project > Click Ok and errors should go away)

# Rest-Api calls for Partners

The following urls can be used to access web-apis meant to be accessed by partners; 
- getProductInfo: curl -X GET 'https://qasimahmed.me/BookLand/rest/partner/read/getProductInfo?key="PARTNERKEY"&amp;productId=121-3-3434-4545-5'
- getOrdersByPartNumber: curl -X GET 'https://qasimahmed.me/BookLand/rest/partner/read/getOrdersByPartNumber?key="PARTNERKEY"&amp;productId=232-2-4342-2343-3'

(Note - in the urls, key refers to the partner key, used to authenticate a partner in order to fulfill the request. A sample key :~ bc629434-da50-416f-aead-c1c492b88271)

# To Test/Use the project

You can un the project acting as a user, business partner or as a dmin. You can register/signup as a customer or a partner and test or run the functionalities accessible (check access metrix).

(Note - You can not sign up as a administartor, we provide a set of administrator credentials for you below to unlock functionalities available exclusively to the admin user).

# Sample credentials you can use

#### User:
qasim@gmail.com
password: 12345678

#### Partner:
jkrowling@gmail.com
password: 12345678 

#### Administrator:
adminofbookstore@gmail.com
password: spongebobsquarepants

# Future Work

- Expand input validation tests (checks for XSS scripting, SQL injections)

# Issues?

Please contact any of the team members (via email provided below) in case you have any issues running the project.

# Team

<strong> Yash Dhamija </strong> - yashdhamija4@gmail.com

<strong> Yonis Abokar </strong> - yabokar@gmail.com

<strong> Qasim Ahmed </strong> - qasimmahir@gmail.com

<strong> Xuan Xiong </strong> - henryxzxiong@hotmail.com

