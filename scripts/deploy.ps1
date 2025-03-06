$microservicename="springmail"

cd ..

mvn clean

mvn package -DskipTests

ssh paas@192.168.1.103 "mkdir -p /home/paas/microservices/$microservicename/"

scp .\target\*.jar paas@192.168.1.103:/home/paas/microservices/$microservicename/
scp .\scripts\* paas@192.168.1.103:/home/paas/microservices/$microservicename/

ssh paas@192.168.1.103 "cd /home/paas/microservices/$microservicename;chmod u+x *.sh;"

mvn clean

cd scripts