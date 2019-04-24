--如果等于0说明超时,其他则是当前资源的访问数量

-- 根据命名读取限流大小,
local expire = 1

-- 先读取资源策略
local limit_count = tonumber(redis.call("get", KEYS[2]))

-- 当前资源限流大小是否未设定,则从redis中读取
if limit_count == nil then
    -- 用户硬编码的限流大小
    limit_count = tonumber(ARGV[1])
end

-- 以当前资源为key生成自增器,过期时间为1s
local current_count = tonumber(redis.call("INCRBY", KEYS[1], expire))

-- 记录当前资源的总请求次数
redis.call("INCRBY", KEYS[3], 1)

local expire_time = tonumber(redis.call('ttl', KEYS[1]))
if expire_time == -1 then
    redis.call("expire", KEYS[1], expire)
    -- 设置当前资源限流后的请求次数,100ms过期
    redis.call('set', KEYS[4], current_count, 'px', 1000)
end
redis.debug(current_count)
redis.debug(limit_count)
if current_count > limit_count then
    -- 设置当前资源限流后的请求次数,100ms过期
    redis.call('set', KEYS[5], current_count, 'px', 1000)
    return 0
else
    -- 设置当前资源限流后的请求次数,100ms过期
    redis.call('set', KEYS[4], current_count, 'px', 1000)
    return current_count
end
