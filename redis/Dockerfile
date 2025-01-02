# Use the official Redis image
FROM redis:7.4.1

# Copy the certificate and key into the container
COPY redis.crt /usr/local/etc/redis/redis.crt
COPY redis.key /usr/local/etc/redis/redis.key
COPY ca.crt /usr/local/etc/redis/ca.crt

# Configure Redis to use TLS
CMD ["redis-server", "--tls-port", "6379", "--port", "0", "--tls-cert-file", "/usr/local/etc/redis/redis.crt", "--tls-key-file", "/usr/local/etc/redis/redis.key", "--tls-ca-cert-file", "/usr/local/etc/redis/ca.crt"]