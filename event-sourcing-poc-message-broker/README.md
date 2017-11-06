# Running KAFKA as message broker

I didn't want to reinvent the wheel, so I included k8s scripts for running Kafka from [another GitHub Project][1] as git submodule (in this case this saved me a lot of time, and I believe I didn't brake any rules/licence).
Original setup there is ment for running Kafka on k8s on AWS, but in case you want to run it on single-cluster k8s on your local machine you have to use [minikube][2].
In case of minikube you should not use LoadBalancer type, use NodePort instead (in kafka-service.yml please replace LoadBalancer with NodePort).
Also, for minikube you have to change advertised host name, set KAFKA_ADVERTISED_HOST_NAME to your service CLUSTER IP minikube assigned (in my case it was 10.0.0.11)
Since this is git submodule it is not bad to update submodule from time to time, call:
```
$ git submodule update --init --recursive
```
Also, pls do not try to commit any changes you did in submodule, not to mention to push them, this is not wanted!!!

## Possible network problems on MacOS
It is possible that you cannot connect to your service hosted on minikube cluster with MacOS X host. In that case please follow this nice [instruction][3] to enable this communication. In my case I use virtualbox (there it was xhyve) so I had to change bridge100 to bridge0. Also, I am runnning Sierra and there was no folder and file /etc/resolver/svc.cluster.local so I had to create that one and put content there as described.
After these changes if you want to access your service you can use service CLUSTER IP, or {service-name}.{namespace-name}:{service-port} (in this kafka example I could access kafka service with curl -k https://kafka-service.kafka-cluster:9092). 

## Testing what is comming to kafka
To test what is comming to my kafka-service I have used kafkacat tool
```
brew install kafkacat
```
and DNS name described earlier
```
kafkacat -b kafka-service.kafka-cluster:9092 -t {my_topic}
```

## Running kubectl tool for multiple clusters
When you have minikube installed locally and you have also one in public cloud, you want to stay operational with kubectl tool in all cases. In this case you have to play with ~/.kube/config, or using --kubeconfig option everytime you call kubectl tool.
In my case, I have backed up my config right after starting minikube (minikube prepared everything for me by default).

## Running kafka on localhost (using Apache scripts)
If you have any problems starting kafka on your developer machine, just use description from official [Apache site][4].
In that case you can consume messages with
```
kafkacat -b 0.0.0.0:9092 -t accountChange
```

[1]:https://github.com/ramhiser/kafka-kubernetes
[2]:https://kubernetes.io/docs/getting-started-guides/minikube/
[3]:https://stevesloka.com/2017/05/19/access-minikube-services-from-host/
[4]:https://kafka.apache.org/quickstart
