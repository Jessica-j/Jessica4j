package org.jessica.core.auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 以ThreadLocal方式实现Web端登录信息传递到业务层的存取
 */
public class AuthContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(AuthContextHolder.class);

    public static final String DEFAULT_UNKNOWN_PIN = "N/A";

    /**
     * 获取SYS类型账户登录账号
     */
    public static String getAuthSysUserUid() {
        AuthUserDetails authUserDetails = getAuthUserDetails();
        if (authUserDetails == null )   return null;
        return authUserDetails.getAuthUid();
    }

    /**
     * 获取登录用户的友好显示字符串
     */
    public static String getAuthUserDisplay() {
        AuthUserDetails authUserDetails = getAuthUserDetails();
        if (authUserDetails != null) {
            return authUserDetails.getAuthDisplay();
        }
        return DEFAULT_UNKNOWN_PIN;
    }

    /**
     * 基于Spring Security获取用户认证信息
     */
    public static AuthUserDetails getAuthUserDetails() {
        Subject subject = null;
        try {
            subject = SecurityUtils.getSubject();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        if (subject == null) {
            return null;
        }
        Object principal = subject.getPrincipal();
        if (principal == null) {
            return null;
        }
        return (AuthUserDetails) principal;
    }

    /**
     * 获取当前登录用户Entity实体对象
     */
    public static User findAuthUser() {
        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (authUserDetails == null) {
            return null;
        }
        UserService userService = SpringContextHolder.getBean(UserService.class);
        return userService.findByAuthTypeAndAuthUid(authUserDetails.getAuthType(), authUserDetails.getAuthUid());
    }
}
