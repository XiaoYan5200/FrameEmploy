package com.frameback.frameback.controller;

import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author 乔乾彭
 * @desc
 * @date 2025/5/26
 */
public class UserController {
    @Resource
    private final RestTemplate restTemplate;

    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }
}