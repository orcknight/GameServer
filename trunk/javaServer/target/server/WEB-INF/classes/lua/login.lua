--无参函数
function hello()
    print 'hello'
end
--带参函数
function test(str)
    print('data from java is:'..str)
    return 'haha'
end

function luaPrint()

    local logger = luajava.newInstance("com.tian.server.util.LivingLuaAgent")
    --调用对象方法
    logger:info("Test call java in lua0")
end