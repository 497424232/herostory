package org.tinygame.herostory;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 创建sqlSessionFactory
 */
public final class MySqlSessionFactory {

    private final Logger logger = LoggerFactory.getLogger(MySqlSessionFactory.class);

    private static SqlSessionFactory sqlSessionFactory;

    private MySqlSessionFactory() {

    }

    /**
     * 初始化
     */
    public static void init() {
        try {
            sqlSessionFactory = (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsStream("MyBatisConfig.xml"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static SqlSession openSession() {
        if (null == sqlSessionFactory) {
            throw new RuntimeException("_sqlSessionFactory 尚未初始化");
        }
        return sqlSessionFactory.openSession(true);
    }
}
