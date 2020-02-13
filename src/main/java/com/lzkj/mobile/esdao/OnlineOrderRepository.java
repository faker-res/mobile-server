package com.lzkj.mobile.esdao;

import com.lzkj.mobile.esdao.OnLineOrderEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OnlineOrderRepository extends ElasticsearchRepository<OnLineOrderEs, Integer> {

    List<OnLineOrderEs> findByUserIDIn(List<Integer> userIdList);
    OnLineOrderEs findByOnLineID(Integer OnLineID);//根据主键ID查询
    OnLineOrderEs findByOrderID(String orderID);//根据orderID查询


}
