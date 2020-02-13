package com.lzkj.mobile.esdao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AccountsInfoRepository extends ElasticsearchRepository<AccountsInfoEs, Integer> {

     List<AccountsInfoEs> findByParentID(Integer parentID);


 }
