# Running KAFKA as message broker

I didn't want to reinvent the wheel, so I included k8s scripts for running Kafka from [another GitHub Project][1] as git submodule (in this case this saved me a lot of time, and I believe I didn't brake any rules/licence).
Original setup there is ment for running Kafka on k8s on AWS, but in case you want to run it on single-cluster k8s on your local machine you have to use [minikube][2].
In case of minikube you should not use LoadBalancer type, use NodePort instead (in kafka-service.yml please replace LoadBalancer with NodePort).

[1]:https://github.com/ramhiser/kafka-kubernetes
[2]:https://kubernetes.io/docs/getting-started-guides/minikube/
