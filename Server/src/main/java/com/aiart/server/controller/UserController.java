package com.aiart.server.controller;

import com.aiart.server.entity.UserEntity;
import com.aiart.server.service.UserApplicationService;
import com.aiart.server.service.UserService;
import com.aiart.server.utils.LoggerInfo;
import com.aiart.server.utils.LoginResultInfo;
import com.aiart.server.utils.UserResult;
import com.aiart.server.utils.SmsCodeManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@Api(tags = "User Controller", description = "Controller for user-related operations")
@RequestMapping("/api/user") // 定义请求的路径前缀
public class UserController {

    private final UserService userService;
    private final SmsCodeManager smsCodeManager;
    private final UserApplicationService userApplicationService;
    private final LoggerInfo LoggerInfoTo;
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final boolean outputToConsole=true;//true-输出到控制台，false输出到journal日志

    @Autowired
    public UserController(UserService userService, SmsCodeManager smsCodeManager, UserApplicationService userApplicationService) {
        this.userService = userService;
        this.smsCodeManager = smsCodeManager;
        this.userApplicationService = userApplicationService;
        this.LoggerInfoTo = new LoggerInfo(outputToConsole);
    }

    @ApiOperation("User Login")
    @PostMapping("/login") // 定义登录接口的路径和请求类型，密码登录
    public ResponseEntity<LoginResultInfo> login(@ApiParam(value = "User account", required = true) @RequestParam("account") String account,
    @ApiParam(value = "User password", required = true) @RequestParam("password") String password) {
        // 根据账号和密码进行登录验证
        LoginResultInfo result = userService.login(account, password);

        // 调用切换日志输出位置的方法
        LoggerInfoTo.toggleLogOutput();
        if (result != null) {
            logger.info("Login successful: "+account);
            return ResponseEntity.ok(result);
        } else {
            logger.error("Login failed for account: "+account);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @ApiOperation("User Registration")
    @PostMapping("/register")//密码注册
    public ResponseEntity<UserResult> register(
            @ApiParam(value = "User account", required = true) @RequestParam("account") String account,
            @ApiParam(value = "User password", required = true) @RequestParam("password") String password){
        // 添加日志信息
        logger.info( "Received registration request for account: " + account);

        UserResult result = userService.register(account, password);

        // 记录注册结果
        logger.info("Registration result for account " + account + ": " + result);

        return switch (result) {
            case SUCCESS -> ResponseEntity.ok(UserResult.SUCCESS);
            case ACCOUNT_EXISTS -> ResponseEntity.ok(UserResult.ACCOUNT_EXISTS);
            // 注册失败
            default -> ResponseEntity.ok(UserResult.SERVER_ERROR);
        };
    }

    @ApiOperation("User SMS Login")
    @PostMapping("/sms-login")  // 验证验证码并login
    public ResponseEntity<UserResult> smsLogin(@ApiParam(value = "Phone number", required = true) @RequestParam("phoneNumber") String phoneNumber,
                                               @ApiParam(value = "SMS code", required = true) @RequestParam("smsCode") String code) {
        // 添加日志信息
        logger.info( "Received SMS login request for phone number: " + phoneNumber);
        logger.info("code:"+code);
        if (smsCodeManager.validateCode(phoneNumber, code)) {
            // 验证通过，执行登录操作
            UserResult result = userService.smsLogin(phoneNumber);
            logger.info( "SMS login successful for phone number: " + phoneNumber);
            // 删除验证码，确保一次性使用
            smsCodeManager.removeCode(phoneNumber);
            return ResponseEntity.ok(result);
        } else {
            // 验证码不正确
            logger.error( "Invalid SMS code for phone number: " + phoneNumber);
            return ResponseEntity.ok(UserResult.INVALID_CREDENTIALS);
        }
    }
    @ApiOperation("User SMS Register")
    @PostMapping("/sms-login-register")  // 验证验证码并register
    public ResponseEntity<UserResult> smsRegister(@ApiParam(value = "Phone number", required = true) @RequestParam("phoneNumber") String phoneNumber,
                                               @ApiParam(value = "SMS code", required = true) @RequestParam("smsCode") String code,
                                                  @ApiParam(value = "Password", required = true) @RequestParam("password") String password ) {
        // 添加日志信息
        logger.info( "Received SMS register request for phone number: " + phoneNumber);
        logger.info("code:"+code);
        if (smsCodeManager.validateCode(phoneNumber, code)) {
            // 验证通过，执行登录操作

            UserResult result = userService.register(phoneNumber,password);
            logger.info( "SMS register successful for phone number: " + phoneNumber);
            // 删除验证码，确保一次性使用
            smsCodeManager.removeCode(phoneNumber);

            return ResponseEntity.ok(result);
        } else {
            // 验证码不正确
            logger.error( "Invalid SMS code for phone number: " + phoneNumber);
            return ResponseEntity.ok(UserResult.INVALID_CREDENTIALS);
        }
    }
    @ApiOperation("Send SMS Code for Login")
    @PostMapping("/send-sms-code")  // login 发送验证码
    public ResponseEntity<UserResult> sendSmsCode(@ApiParam(value = "Phone number", required = true) @RequestParam("phoneNumber") String phoneNumber) {
        // 调用切换日志输出位置的方法
//        toggleLogOutput(outputToConsole);
        // 添加日志信息
        logger.info( "Sending SMS code to phone number: " + phoneNumber);

        UserResult result = userService.isPhoneNumberRegistered(phoneNumber);
        if (result == UserResult.ACCOUNT_NOT_FOUND) {
            logger.warn("Phone number not found: " + phoneNumber);
            return ResponseEntity.ok(UserResult.ACCOUNT_NOT_FOUND);
        }

        // 生成和发送验证码
        String code = smsCodeManager.generateCode();
        try {
            smsCodeManager.sendCode(phoneNumber, code);
            logger.info( "SMS code sent successfully to phone number: " + phoneNumber);
        } catch (ExecutionException | InterruptedException e1) {
            logger.error("Failed to send SMS code to phone number: " + phoneNumber, e1);
            return ResponseEntity.ok(UserResult.SERVER_ERROR);
        }

        // 保存验证码以便后续验证
        smsCodeManager.saveCode(phoneNumber, code);
        // 返回成功
        return ResponseEntity.ok(UserResult.SUCCESS);

    }

    @ApiOperation("Send SMS Code for Registration")
    @PostMapping("/register-send-sms-code")  // register发送验证码
    public ResponseEntity<UserResult> sendSmsCodeInRegister(@ApiParam(value = "Phone number", required = true) @RequestParam("phoneNumber") String phoneNumber) {
        // 添加日志信息
        logger.info( "Sending SMS code during registration to phone number: " + phoneNumber);

        UserResult result = userService.isPhoneNumberRegistered(phoneNumber);
        if (result == UserResult.SUCCESS) {
            logger.info( "Phone number already registered: " + phoneNumber);
            return ResponseEntity.ok(UserResult.ACCOUNT_EXISTS);
        } else {
            // 生成和发送验证码
            String code = smsCodeManager.generateCode();
            try {
                smsCodeManager.sendCode(phoneNumber, code);
                logger.info( "SMS code sent successfully during registration to phone number: " + phoneNumber);
            } catch (ExecutionException | InterruptedException e1) {
                logger.error( "Failed to send SMS code during registration to phone number: " + phoneNumber, e1);
                return ResponseEntity.ok(UserResult.SERVER_ERROR);
            }

            // 保存验证码以便后续验证
            smsCodeManager.saveCode(phoneNumber, code);

            return ResponseEntity.ok(UserResult.SUCCESS);
        }

    }
    @ApiOperation("Get User Applications")
    @GetMapping("/applications/{userId}")
    public ResponseEntity<List<Long>> getUserApplications(@ApiParam(value = "User ID", required = true,example = "123") @PathVariable Long userId) {
        // 记录请求信息
        logger.info("Received request to fetch applications for user ID: "+userId);

        List<Long> applicationIds = userApplicationService.getApplicationsByUserId(userId);

        if (applicationIds != null) {
            // 记录成功响应
            logger.info("Retrieved "+applicationIds.size()+" applications for user ID:"+userId);
            return ResponseEntity.ok(applicationIds);
        } else {
            // 记录找不到用户或用户未拥有应用程序的情况
            logger.warn("No applications found for user ID: "+ userId);
            return ResponseEntity.notFound().build();
        }
    }

   //get头像
    @ApiOperation("Get User Profile")
    @GetMapping("/{userId}/get-profile")
    public ResponseEntity<UserEntity> getUserProfile( @ApiParam(value = "User ID", required = true,example = "123") @PathVariable Long userId) {
        // 记录请求信息
        logger.info("Received request to fetch user profile for user ID: "+userId);

        // 根据 userId 从数据库或其他数据源中获取用户信息
        UserEntity user = userService.getUserById(userId);

        if (user != null) {
            // 记录成功响应
            logger.info("Retrieved user profile for user ID: "+ userId);
            return ResponseEntity.ok(user);
        } else {
            // 记录找不到用户的情况
            logger.warn("User not found for user ID: "+ userId);
            return ResponseEntity.ok(null);
        }
    }

    // 修改用户昵称和性别
    @ApiOperation("Update User Info")
    @PutMapping("/{userId}/userinfo")
    public ResponseEntity<UserResult> updateUserInfo(
            @ApiParam(value = "User ID", required = true,example = "123") @PathVariable Long userId,
            @ApiParam(value = "New nickname", required = true) @RequestParam String newNickname,
            @ApiParam(value = "New gender", required = true) @RequestParam String newGender){
        // 记录请求信息
        logger.info("Received request to update user info for user ID: "+ userId);
        boolean updated = userService.updateUserInfo(userId, newNickname, newGender);

        if (updated) {
            // 记录成功更新用户信息
            logger.debug("User information updated successfully for user ID: "+ userId);
            return ResponseEntity.ok(UserResult.SUCCESS);
        } else {
            // 记录找不到用户的情况
            logger.warn("User not found for user ID: "+ userId);
            return ResponseEntity.ok(UserResult.ACCOUNT_NOT_FOUND);
        }
    }
}

