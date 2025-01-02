openssl genpkey -algorithm RSA -out ca.key -pkeyopt rsa_keygen_bits:4096
openssl req -x509 -new -nodes -key ca.key -sha256 -days 365 -out ca.crt -config ca_openssl.cnf

openssl genpkey -algorithm RSA -out redis.key -pkeyopt rsa_keygen_bits:4096
openssl req -new -key redis.key -out redis.csr -config openssl.cnf
openssl x509 -req -in redis.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out redis.crt -days 3650 -sha256 -extfile openssl.cnf -extensions req_ext

docker build -t redis-tls:7.4.1 .
docker run -d --name redis -p 6379:6379 -e REDIS_PASSWORD=changeit redis-tls:7.4.1 --tls-port 6379 --port 0 --tls-cert-file /usr/local/etc/redis/redis.crt --tls-key-file /usr/local/etc/redis/redis.key --tls-ca-cert-file /usr/local/etc/redis/ca.crt --requirepass changeit --tls-auth-clients no

redis-cli --tls --cert /usr/local/etc/redis/redis.crt --key /usr/local/etc/redis/redis.key --cacert /usr/local/etc/redis/redis.crt -a changeit
redis-cli -h 172.17.0.3 --tls --cert ./redis/redis.crt --key ./redis/redis.key --cacert ./redis/redis.crt -a changeit

# Convert the certificate to a truststore (redis.jks)
keytool -import \
    -alias redis \
    -file ca.crt \
    -keystore redis.jks \
    -storepass changeit \
    -noprompt

https://redis.github.io/lettuce/advanced-usage/#ssl-connections

openssl pkcs12 -export -in redis.crt -inkey redis.key -out redis.p12 -name "redis" -passout pass:changeit
keytool -importkeystore -srckeystore redis.p12 -srcstoretype PKCS12 -destkeystore redis.jks -deststoretype JKS -deststorepass changeit