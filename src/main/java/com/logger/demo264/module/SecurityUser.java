package com.logger.demo264.module;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author 风尘
 * @date 2022-07-18
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class SecurityUser {
    private Integer id;
    private String username;
}
