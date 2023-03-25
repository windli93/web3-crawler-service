package cn.com.web3.logic.dao;

import cn.com.web3.logic.entity.EthChainConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EthChainRepository extends JpaRepository<EthChainConfig, Integer> {

//    EthChainConfig findByImageIdAndStatus(Integer imageId, Integer status);
}