package cn.com.web3.logic.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author hongjian.li
 * @Description 获取钱包地址和代理IP关联的配置
 * @Date 2023/3/25 14:12
 **/
@Data
@Entity
@Table(name = "t_eth_address_config",
        indexes = {
                @Index(name = "index_eth_address_id", unique = true, columnList = "eth_address_id")
        })
public class EthChainConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    @Column(name = "id", length = 11, nullable = false)
    private Long id;

    @Column(name = "eth_address_id", length = 11, nullable = false)
    private Integer ethAddressId;

    @Column(name = "eth_public_address", columnDefinition = "varchar(1000) not null default ''")
    private String ethPublicAddress;

    @Column(name = "eth_private_key", columnDefinition = "varchar(500) not null default ''")
    private String ethPrivateKey;

    @Column(name = "proxy_ip", columnDefinition = "varchar(30) not null default ''")
    private Integer proxyIp;

    @Column(name = "proxy_port", columnDefinition = "Integer(11) not null default 443")
    private Integer proxyPort;

    @Column(name = "failHourNum", columnDefinition = "Integer(11) not null default 0")
    private Integer failHourNum;

    @UpdateTimestamp
    @Column(name = "update_time", columnDefinition = "timestamp not null default current_timestamp")
    private Date updateTime;

    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "timestamp not null default current_timestamp")
    private Date createTime;

    @Column(name = "status", columnDefinition = "tinyint(1) not null default 1")
    private Integer status;
}
