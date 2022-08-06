Running mongodb pre requirements

1. Docker installed on mac machine.
2. Downloading mongo image:
   1. `docker run -d -p 27018:27017 -v /Users/mayanksinghrana/personal/mongo-docker:/data/db --name dockermongo mongo:latest`
      
        Explanation : `27018:17017` --> port mapping
    <br> `-v /Users/mayanksinghrana/personal/mongo-docker:/data/db` -- path used as volume to store mongodb documents
   <br> `--name` --> name of the container
   <br> `mongo:latest` --> image:version number

Above steps will run the mongodb server on localhost at `27018` port <br>
3. For Login in mongodb
   <br> `docker exec -it dockermongo bash`
    <br> It will redirect to mongo terminal