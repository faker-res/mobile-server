package com.lzkj.mobile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;

@Configuration
@ConfigurationProperties(prefix="spring.data.mongodb.game")
public class GameMongoDB {
	@Value("${spring.data.mongodb.game.uri}")
	private String url;

    @Bean(name="gameMongoTemplate")
    public MongoTemplate getMongoTemplate() {
        return new MongoTemplate(new SimpleMongoDbFactory(new MongoClientURI(url,MongoClientOptions.builder().maxConnectionIdleTime(30000).connectTimeout(5000))));
    }
}

