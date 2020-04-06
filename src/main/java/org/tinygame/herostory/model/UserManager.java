package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户管理器
 *
 * @auther changmk
 * @date 2020/2/9 下午2:52
 */
public final class UserManager {

    /**
     * 用户字典
     * todo 第六课在使用线程池后，用ConcurrentHashMap
     */
    private static final Map<Integer, User> userMap = new ConcurrentHashMap<>();

    private UserManager(){

    }

    /**
     * 添加用户
     * @param newUser
     */
    public static void addUser(User newUser) {
        if (null != newUser) {
            userMap.put(newUser.getUserId(), newUser);
        }
    }

    /**
     * 移除用户
     * @param userId
     */
    public static void removeUser(int userId) {
        userMap.remove(userId);
    }

    /**
     * 返回所有用户
     * @return
     */
    public static Collection<User> listUser() {
        return userMap.values();
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    public static User getUserById(Integer userId) {
        return userMap.get(userId);
    }
}
