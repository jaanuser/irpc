package com.jaan.irpc;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName RpcRefrenceFactroy
 * @Description
 * @Author jaan
 * @Date 2022/10/16 0:38
 */
public class RpcRefrenceFactroy {


    private static Object lock = new Object();

    private static final Map<Class, Object> rpcRefrenceMap = new ConcurrentHashMap();

    public static <T> T getRpcRefrence(Class<T> clazz) {
        Object rpcRefrence = rpcRefrenceMap.get(clazz);

        if (null != rpcRefrence) {
            return (T) rpcRefrence;
        }

        synchronized (lock) {
            if (null != (rpcRefrence = rpcRefrenceMap.get(clazz))) {
                return (T) rpcRefrence;
            }
            rpcRefrence = createRpcRefrence(clazz);
            rpcRefrenceMap.put(clazz, rpcRefrence);

        }

        return (T) rpcRefrence;

    }

    private static <T> T createRpcRefrence(Class<T> clazz) {

        return (T) Proxy.newProxyInstance(RpcRefrenceFactroy.class.getClassLoader(),
                new Class[]{clazz}, new RpcInvocationHandler());
    }
}
