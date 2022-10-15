package com.jaan.irpc;

import com.jaan.irpc.common.RpcProtocol;
import com.jaan.irpc.common.RpcRequest;
import com.jaan.irpc.common.utils.JsonUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import static com.jaan.irpc.Client.REQ_QUEUE;
import static com.jaan.irpc.Client.RET_MAP;

/**
 * @ClassName RpcInvocationHandler
 * @Description
 * @Author jaan
 * @Date 2022/10/16 0:53
 */
public class RpcInvocationHandler implements InvocationHandler {


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        Class<?> declaringClass = method.getDeclaringClass();
        // Object 中声明的方法不代理
        if (Object.class.equals(declaringClass)) {
            // 如果方法返回类型为void呢？
            return method.invoke(proxy, args);
        }
        rpcRequest.setTargetServiceName(declaringClass.getName());
        rpcRequest.setTargetMethod(method.getName());
        rpcRequest.setUuid(UUID.randomUUID().toString());

        RET_MAP.put(rpcRequest.getUuid(), rpcRequest);
        REQ_QUEUE.add(new RpcProtocol(JsonUtil.toJson(rpcRequest).getBytes()));
        RpcRequest rpcRst = RET_MAP.get(UUID.randomUUID());
        return rpcRst.getResponse();
    }
}
