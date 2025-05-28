//package com.whitecloud.consumer.Config;
//
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.loadbalancer.DefaultResponse;
//import org.springframework.cloud.client.loadbalancer.EmptyResponse;
//import org.springframework.cloud.client.loadbalancer.Request;
//import org.springframework.cloud.client.loadbalancer.Response;
//import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
//import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
//import reactor.core.publisher.Mono;
//
//import java.util.Random;
//
///**
// * 简单自定义的权重负载均衡示例（需根据业务权重调整）
// */
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.loadbalancer.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.Random;
//
//@Configuration
//public class LoadBalancerConfig {
//
//    @Bean
//    public ReactorServiceInstanceLoadBalancer loadBalancer(Environment environment,
//                                                           LoadBalancerClientFactory factory) {
//        String serviceId = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
//
//        // 轮询策略（默认启用）
//        return new RoundRobinLoadBalancer(factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
//
//        // 随机策略
//        /*
//        return new RandomLoadBalancer(factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
//        */
//
//        // 权重策略（简单示例，需要你根据实际权重逻辑改写）
//        /*
//        return new WeightedLoadBalancer(factory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class), serviceId);
//        */
//    }
//
//    /**
//     * 简单自定义的权重负载均衡示例（需根据业务权重调整）
//     */
//    static class WeightedLoadBalancer implements ReactorServiceInstanceLoadBalancer {
//
//        private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
//        private final String serviceId;
//        private final Random random;
//
//        public WeightedLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
//                                    String serviceId) {
//            this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
//            this.serviceId = serviceId;
//            this.random = new Random();
//        }
//
//        @Override
//        public Mono<Response<ServiceInstance>> choose(Request request) {
//            ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable();
//            if (supplier == null) {
//                return Mono.just(new EmptyResponse());
//            }
//
//            return supplier.get().next().map(serviceInstances -> {
//                if (serviceInstances.isEmpty()) {
//                    return new EmptyResponse();
//                }
//                // 简单示例：假设所有实例权重相同，随机选择
//                // 你可以改成根据实例元数据中配置的权重选择
//                int index = random.nextInt(serviceInstances.size());
//                return new DefaultResponse(serviceInstances.get(index));
//            });
//        }
//    }
//}