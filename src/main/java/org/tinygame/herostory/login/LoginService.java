package org.tinygame.herostory.login;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MySqlSessionFactory;
import org.tinygame.herostory.login.db.IUserDao;
import org.tinygame.herostory.login.db.UserEntity;
import sun.swing.StringUIClientPropertyKey;

/**
 * 用户登录Service
 */
public class LoginService {

    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    /**
     * 单例对象
     */
    static private final LoginService _instance = new LoginService();

    /**
     * 私有化类默认构造器
     */
    private LoginService() {
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    static public LoginService getInstance() {
        return _instance;
    }

    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    public UserEntity userLogin(String userName, String password) {
        if (null == userName || null == password) {
            return null;
        }
        //todo sqlSession放在try括号中，不用手写close方法关闭连接了。
        try (SqlSession sqlSession = MySqlSessionFactory.openSession()) {
            //todo 获取dao 注意无spring框架获取dao的方法
            IUserDao userDao = sqlSession.getMapper(IUserDao.class);
            UserEntity userEntity = userDao.getUserByName(userName);
            if (null != userEntity) {
                if (!password.equals(userEntity.password)) {
                    LOGGER.error("用户名密码错误，userName={}", userName);
                }
                throw new RuntimeException("用户名密码错误");
            } else {
                userEntity = new UserEntity();
                userEntity.userName = userName;
                userEntity.password = password;
                userEntity.heroAvatar = "Hero_Shaman";
                userDao.insertInto(userEntity);
            }
            return userEntity;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

}
