Failed running devcontainer at first time with error: 

```
failed to solve: failed to resolve source metadata for docker.io/docker/dockerfile:1.4: failed to authorize: failed to fetch o
auth token: Post "https://auth.docker.io/token"
```

Had to manually pull the image docker/dockerfile:1.4 and run devcontainer again.

java -XshowSettings:properties -version
/usr/lib/jvm/msopenjdk-current

sudo su -
keytool -delete -cacerts -alias redis -storepass changeit -noprompt
keytool -importcert -file /workspaces/k8s-caching/redis/ca.crt -cacerts -alias redis -storepass changeit -noprompt

keytool -list -alias redis -cacerts -storepass changeit -noprompt

keytool -importkeystore -srckeystore redis.p12 -srcstoretype PKCS12 -destkeystore redis.jks -deststoretype JKS -deststorepass changeit
