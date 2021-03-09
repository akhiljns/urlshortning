# urlshortning
project to shorten the url

requirements - 

docker
redis
maven 

## Docker commands
## step1 - Pulling redis image from docker hub:
docker pull redis

## steps to build the image and run it:

- mvn clean install
- docker-compose up --build

## Installed images from docker hub 

redis db image            - https://hub.docker.com/r/akhiljns/redis <br />
url shortner app image    - https://hub.docker.com/r/akhiljns/urlshortner <br />

 these images can be pulled and run directly on local docker containers <br />

## they can be pulled by 
docker pull akhiljns/redis:v1 <br />
docker pull akhiljns/urlshortner:v1 <br />

## finally after pulling both the images, you can run them by 
docker run akhiljns/redis:v1 <br />
docker run akhiljns/urlshortner:v1 <br />


# testing the project using postman

refer urlshortner.postman_collection.json file <br />

POST request to generate the shorten url <br />

headers : content-type application/json

url : http://localhost:10095/rest/url

json body :

{
	"url" : "https://www.facebook.com/"
}

<br />
response { shortned Url = https://shorturl/0e2d9959 }

where id is 0e2d9959 i.e. https://shorturl/{id}

<br /><br />

GET request to fetch original url from id generated

0e2d9959 is the {id}  

http://localhost:10095/rest/url/0e2d9959



