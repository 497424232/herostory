package org.tinygame.herostory.login;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MySqlSessionFactory;
import org.tinygame.herostory.async.AsyncOperationProcessor;
import org.tinygame.herostory.async.IAsyncOperation;
import org.tinygame.herostory.login.db.IUserDao;
import org.tinygame.herostory.login.db.UserEntity;
import sun.swing.StringUIClientPropertyKey;

import java.util.function.Function;

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
     * @param callback 回调方法
     * @return
     */
    public void userLogin(String userName, String password, Function<UserEntity, Void> callback) {
        if (null == userName || null == password) {
            return;
        }

        AsyncGetUserByName asyncGetUserByName = new AsyncGetUserByName(userName, password) {
            @Override
            public void doFinish() {
                //todo apply 方法在userLogin 方法调用实现处
                callback.apply(this.getUserEntity());
            }
        };

        AsyncOperationProcessor.getInstance().process(asyncGetUserByName);
    }

    private class AsyncGetUserByName implements IAsyncOperation{

        private String _userName;
        private String _password;

        private UserEntity _userEntity;

        public AsyncGetUserByName(String userName, String password) {
            this._userName = userName;
            this._password = password;
        }

        public UserEntity getUserEntity() {
            return _userEntity;
        }

        @Override
        public void doAsync() {

            LOGGER.info("用户登录线程："+ Thread.currentThread().getName());

            //todo sqlSession放在try括号中，不用手写close方法关闭连接了。
            try (SqlSession sqlSession = MySqlSessionFactory.openSession()) {
                //todo 获取dao 注意无spring框架获取dao的方法
                IUserDao userDao = sqlSession.getMapper(IUserDao.class);
                UserEntity userEntity = userDao.getUserByName(_userName);
                if (null != userEntity) {
                    if (!_password.equals(userEntity.password)) {
                        LOGGER.error("用户名密码错误，userName={}", _userName);
                    }
//                    throw new RuntimeException("用户名密码错误");
                } else {
                    userEntity = new UserEntity();
                    userEntity.userName = _userName;
                    userEntity.password = _password;
                    userEntity.heroAvatar = "Hero_Shaman";
                    userDao.insertInto(userEntity);
                }
                this._userEntity = userEntity;
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
}
