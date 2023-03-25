package cn.com.web3.logic.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author hongjian.li
 * @Description 存储SheinCookie相关情况
 * @Date 2022/10/12 13:38
 **/
@Data
@Entity
@Table(name = "t_shein_cookie_config",
        indexes = {
                @Index(name = "idx_cookie_id", unique = true, columnList = "shein_cookie_id")
        })
public class SheinCookieConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    @Column(name = "id", length = 11, nullable = false)
    private Long id;

    @Column(name = "shein_cookie_id", length = 11, nullable = false)
    private Integer sheinCookieId;

    @Column(name = "cookie_info", columnDefinition = "TEXT not null")
    private String cookieInfo;

    @Column(name = "csrf_token", columnDefinition = "varchar(500) not null")
    private String csrfToken;

    @Column(name = "max_fail_num", columnDefinition = "Integer(10) not null default 0")
    private Integer maxFailNum;

    @Column(name = "min_fail_num", columnDefinition = "Integer(10) not null default 0")
    private Integer minFailNum;

    @UpdateTimestamp
    @Column(name = "update_time", columnDefinition = "timestamp not null default current_timestamp")
    private Date updateTime;

    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "timestamp not null default current_timestamp")
    private Date createTime;

    @Column(name = "status", columnDefinition = "tinyint(1) not null default 1")
    private Integer status;
}
