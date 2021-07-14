package com.ants.modules.filter;//package com.ants.modules.system.filter;
//
//import io.micrometer.core.instrument.MeterRegistry;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * TODO
// * Author Chen
// * Date   2021/3/31 10:42
// */
//@Configuration
//@ConditionalOnWebApplication
//public class HttpTraceConfiguration {
//
//    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//    static class ServletTraceFilterConfiguration {
//
//        @Bean
//        public HttpTraceLogFilter httpTraceLogFilter(MeterRegistry registry) {
//            return new HttpTraceLogFilter(registry);
//        }
//
//    }
//
//}