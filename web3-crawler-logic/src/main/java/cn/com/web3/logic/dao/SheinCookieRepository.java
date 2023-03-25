package cn.com.web3.logic.dao;

import cn.com.web3.logic.entity.SheinCookieConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheinCookieRepository extends JpaRepository<SheinCookieConfig, Integer> {

    /**
     * @Author hongjian.li
     * @Description 通过Cookie获取信息
     **/
//    SheinCookieConfig findBySheinCookieIdAndStatus(Integer sheinCookieId, Integer status);
}