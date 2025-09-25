package com.frameback.frameback.controller;

import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author 乔乾彭
 * @desc
 * @date 2025/5/26
 */
public class UserController {
    private RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}