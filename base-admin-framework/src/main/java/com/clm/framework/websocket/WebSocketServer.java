package com.clm.framework.websocket;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务
 *
 * @author 陈黎明
 * @since 2025-09-10
 */
@ServerEndpoint("/ws/{token}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 用于存储用户ID和WebSocket连接的映射关系
     */
    private static final Map<Long, WebSocketServer> ONLINE_USERS = new ConcurrentHashMap<>();
    /**
     * JSON处理器
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 发送消息给指定用户
     *
     * @param userId  用户ID
     * @param message 消息内容
     */
    public static void sendMessageToUser(Long userId, String message) {
        WebSocketServer server = ONLINE_USERS.get(userId);
        if (server != null) {
            server.sendMessage(message);
        }
    }

    /**
     * 发送消息对象给指定用户（会自动转换为JSON字符串）
     *
     * @param userId  用户ID
     * @param message 消息对象
     */
    public static void sendMessageToUser(Long userId, Object message) {
        WebSocketServer server = ONLINE_USERS.get(userId);
        if (server != null) {
            server.sendMessage(message);
        }
    }

    /**
     * 发送消息给所有用户
     *
     * @param message 消息内容
     */
    public static void sendMessageToAll(String message) {
        ONLINE_USERS.values().forEach(server -> server.sendMessage(message));
    }

    /**
     * 发送消息对象给所有用户（会自动转换为JSON字符串）
     *
     * @param message 消息对象
     */
    public static void sendMessageToAll(Object message) {
        ONLINE_USERS.values().forEach(server -> server.sendMessage(message));
    }

    /**
     * 判断用户是否在线
     *
     * @param userId 用户ID
     * @return 是否在线
     */
    public static boolean isUserOnline(Long userId) {
        return ONLINE_USERS.containsKey(userId);
    }

    /**
     * 获取当前在线人数
     *
     * @return 在线人数
     */
    public static int getOnlineCount() {
        return ONLINE_USERS.size();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            // 验证token并获取用户ID
            Object loginIdObj = StpUtil.getLoginIdByToken(token);
            if (loginIdObj == null) {
                sendMessage("无效的token，连接关闭");
                session.close();
                return;
            }

            this.userId = Long.parseLong(loginIdObj.toString());
            this.session = session;

            // 如果用户已经在线，先移除旧连接
            if (ONLINE_USERS.containsKey(userId)) {
                WebSocketServer oldServer = ONLINE_USERS.get(userId);
                oldServer.session.close();
                ONLINE_USERS.remove(userId);
            }

            // 添加到在线用户映射
            ONLINE_USERS.put(userId, this);
            log.info("用户{}连接成功，当前在线人数：{}", userId, ONLINE_USERS.size());

            // 发送连接成功消息
            sendMessage("WebSocket连接成功");
        } catch (Exception e) {
            log.error("WebSocket连接异常", e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (userId != null) {
            ONLINE_USERS.remove(userId);
            log.info("用户{}断开连接，当前在线人数：{}", userId, ONLINE_USERS.size());
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户{}的消息：{}", userId, message);
        // 这里可以处理客户端发送的消息
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误：{}", error.getMessage());
        error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息异常", e);
        }
    }

    /**
     * 发送消息对象（会自动转换为JSON字符串）
     *
     * @param message 消息对象
     */
    public void sendMessage(Object message) {
        try {
            this.session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
        } catch (IOException e) {
            log.error("发送消息异常", e);
        }
    }
}
