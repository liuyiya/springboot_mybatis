package com.evolution.filter;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

/**
 * 日志过滤器
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*", filterName = "LogFilter")
public class LogFilter extends OncePerRequestFilter implements Ordered {

    public static final String SPLIT_STRING_M = "=";

    public static final String SPLIT_STRING_DOT = ", ";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final List<String> EXCLUDE_PATHS = Convert.toList(String.class, "/minio/upload");

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (EXCLUDE_PATHS.contains(servletPath)) {
            printExcludePathsRequest(request);
            filterChain.doFilter(request, response);
            return;
        }
        ContentCachingRequestWrapper wrapperRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrapperResponse = new ContentCachingResponseWrapper(response);
        long startTime = System.currentTimeMillis();
        printRequest(wrapperRequest);
        filterChain.doFilter(wrapperRequest, wrapperResponse);
        log.info("  Request Body   : {}", getRequestBody(wrapperRequest));
        log.info("  ================================================================" + LINE_SEPARATOR);
        printResponse(wrapperResponse, startTime);
        wrapperResponse.copyBodyToResponse();
    }

    private void printExcludePathsRequest(HttpServletRequest request) {
        log.info("=============================  Start  ================================");
        log.info("  =========================== Request ============================");
        log.info("  URL            : {}", request.getRequestURL().toString());
        log.info("  IP             : {}", request.getRemoteAddr());
        log.info("  EXCLUDE_PATHS  : {}", request.getServletPath());
        log.info("=============================  End  ================================");
    }

    private void printRequest(ContentCachingRequestWrapper request) {
        log.info("=============================  Start  ================================");
        log.info("  =========================== Request ============================");
        log.info("  URL            : {}", request.getRequestURL().toString());
        log.info("  HTTP Method    : {}", request.getMethod());
        log.info("  IP             : {}", request.getRemoteAddr());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("  {}: {}", StrUtil.fillAfter(StrUtil.upperFirst(headerName), ' ', 15), request.getHeader(headerName));
        }
        log.info("  Request Args   : {}", getRequestParams(request));
    }

    private void printResponse(ContentCachingResponseWrapper response, long startTime) {
        log.info("  =========================== Response ===========================");
        log.info("  Status Code       : {}", response.getStatusCode());
        Collection<String> headerNames = response.getHeaderNames();
        headerNames.forEach(l -> log.info("  {}: {}", StrUtil.fillAfter(StrUtil.upperFirst(l), ' ', 15), response.getHeader(l)));
        log.info("  Response Body     : {}", getResponseBody(response));
        log.info("  Time-Consuming    : {} ms", System.currentTimeMillis() - startTime);
        log.info("  ================================================================");
    }

    /**
     * 打印请求参数
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    payload = "[unknown]";
                }
                return payload.replaceAll("\\r\\n", "");
            }
        }
        return "";
    }

    /**
     * 打印请求参数
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                payload = new String(buf, StandardCharsets.UTF_8);
                return payload;
            }
        }
        return "";
    }

    /**
     * 获取请求地址上的参数
     */
    public static String getRequestParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> enu = request.getParameterNames();
        //获取请求参数
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            sb.append(name).append(SPLIT_STRING_M).append(request.getParameter(name));
            if (enu.hasMoreElements()) {
                sb.append(SPLIT_STRING_DOT);
            }
        }
        return sb.toString();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
