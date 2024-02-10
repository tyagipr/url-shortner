# web services assignment - 1 

## Requirements
Java version 17

Framework used - Springboot 

## Steps to run the code - 
Clone the project.

Make sure all the maven dependencies are installed which bundled up in the cloned project.

Go to the UrlshortnerApplication.java and run the main application in intellij or eclipse etc (editor of your choice)

## Endpoints
1. POST /api/v1/post

  Payload -
   {
     "longUrl": "https://medium.com/@tyagipragya99",
      "userId" : "pty202"
   }

2. GET /api/v1/get?id=#identifier

3. PUT /api/v1/modify?id=#identifier

   Payload -
   {
     "longUrl": "https://medium12345.com/@tyagipragya99"
   }

4. DELETE /api/v1/delete?id=#identifier




