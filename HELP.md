nohup  /ants/minio/minio server --console-address '0.0.0.0:9001' /ants/minio/data/  &



/usr/local/nginx/sbin/nginx -c /usr/local/nginx/conf/nginx.conf     启动  nginx
/usr/local/nginx/sbin/nginx -s stop			                        停止nginx
/usr/local/nginx/sbin/nginx -s reload                               重启nginx
ps -ef | grep nginx

RabbitMQ
`docker run -d --hostname localhost --name myrabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management
`
