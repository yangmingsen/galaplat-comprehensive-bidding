package com.galaplat.comprehensive.bidding.dao.config;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;

@Component
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.galaplat.comprehensive.bidding.dao.mappers.**",
		"com.galaplat.baseplatform.permissions.dao.mappers.**",
		"com.galaplat.baseplatform.serialnumber.plugin2.dao.mappers.**" ,
		"org.galaplat.baseplatform.file.upload.dao.mappers.**"}, sqlSessionFactoryRef = "mySqlSessionFactory", sqlSessionTemplateRef = "mySqlSessionTemplate")
public class MysqlDataSourceBase {


	protected static final Logger LOGGER = LoggerFactory.getLogger(MysqlDataSourceBase.class);

	@Value("${datasource.url}")
    private String url;

    @Value("${datasource.username}")
    private String userName;

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.driver-class-name}")
    private String driverClassName;

    @Value("${datasource.druid.filters}")
    private String filters;

    @Value("${datasource.druid.initial-size}")
    private int initialSize;

    @Value("${datasource.druid.min-idle}")
    private int minIdle;

    @Value("${datasource.druid.max-active}")
    private int maxActive;

    @Value("${datasource.druid.query-timeout}")
    private int queryTimeout;

    @Value("${datasource.druid.transaction-query-timeout}")
    private int transactionQueryTimeout;

    @Value("${datasource.druid.remove-abandoned-timeout}")
    private int removeAbandonedTimeout;
    
	/**
	 * DataSource
	 */
	@Bean(destroyMethod = "close", name = "mydb")
	protected DruidDataSource buildDataSource() {
		DruidDataSource ds = new DruidDataSource();

		ds.setUrl(this.url);
		ds.setUsername(this.userName);
		ds.setPassword(this.password);
		ds.setDriverClassName(this.driverClassName);

		ds.setInitialSize(this.initialSize);
		ds.setMinIdle(this.minIdle);
		ds.setMaxActive(this.maxActive);
		ds.setQueryTimeout(this.queryTimeout);
		ds.setTransactionQueryTimeout(this.transactionQueryTimeout);
		ds.setRemoveAbandonedTimeout(this.removeAbandonedTimeout);
		//~ 每隔一段时间检查一下连接池是否有效
		ds.setTestWhileIdle(true);
		ds.setValidationQuery("select 1");
		try {
			ds.setFilters(this.filters);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}

		return ds;
	}

	/**
	 * TransactionManager
	 */
	@Bean(name = "mytx")
	protected PlatformTransactionManager createTransactionManager(@Qualifier("mydb") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource); 
	}

	@Bean(name = "mySqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("mydb") DataSource dataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		// 分页插件
		PageInterceptor pageHelper = new PageInterceptor();
		Properties props = new Properties();
		props.setProperty("helperDialect", "mysql");
		props.setProperty("offsetAsPageNum", "false");
		props.setProperty("rowBoundsWithCount", "false");
		props.setProperty("pageSizeZero", "false");

		props.setProperty("reasonable", "false");
		props.setProperty("supportMethodsArguments", "false");
		props.setProperty("returnPageInfo", "none");
		props.setProperty("params", "count=countSql");
		pageHelper.setProperties(props);
		// 添加插件
		sessionFactory.setPlugins(new Interceptor[] { pageHelper });

		 sessionFactory.setTypeAliasesPackage("xxx.mybatis");
		return sessionFactory.getObject();
	}

	@Bean(name = "mySqlSessionTemplate")
	public SqlSessionTemplate amonSqlSessionTemplate(
			@Qualifier("mySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}