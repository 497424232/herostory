package org.tinygame.herostory.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理器
 *
 * @auther changmk
 * @date 2020/2/9 下午2:52
 */
public final class UserManager {

    private static final Map<Integer, User> userMap = new HashMap<>();

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
}
