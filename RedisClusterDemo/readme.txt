安装节点：
10.25.23.101-110

jedis配置参数（配置在zookeeper中）：
#默认命令过期时间（秒）
redis.default.commandtimeout=10000
#连接失败最大重试次数
redis.default.max.redirections=50
#ip:port列表,多个ip:port之间使用逗号(,)分割
#redis.hostAndPort=10.25.23.37:6379,10.25.23.38:6379,10.25.23.39:6379
redis.hostAndPort=10.25.23.102:6379,10.25.23.106:6379,10.25.23.109:6379

redis.pool.maxTotal=20
redis.pool.maxIdle=5
redis.pool.minIdle=1
redis.pool.testOnBorrow=true
redis.pool.testOnReturn=true
redis.pool.testWhileIdle=true
redis.pool.numTestsPerEvictionRun=10
redis.pool.timeBetweenEvictionRunsMillis=60000
redis.pool.maxWaitMillis=3000