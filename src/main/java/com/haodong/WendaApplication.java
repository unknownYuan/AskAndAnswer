package com.haodong;

//import de.codecentric.boot.admin.server.config.EnableAdminServer;
//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.ServerSocket;

//@SpringBootApplication
//public class WendaApplication extends SpringBootServletInitializer {
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(WendaApplication.class);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(WendaApplication.class, args);
//    }
//}


//@EnableTransactionManagement
//@EnableAdminServer
//@NacosPropertySource(dataId = "wenda.service", autoRefreshed = true)
@SpringBootApplication
public class WendaApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WendaApplication.class, args);
        int count = context.getBeanDefinitionCount();
        System.out.println("beans count = " + count);
        int i = 1;
        for (String name:
             context.getBeanDefinitionNames()) {
            System.out.println(i + " " + name +" || " +  context.getBean(name).getClass());
//            if(name.equals("druid")){
//                System.out.println();
//            }
            i++;
        }
        System.out.println("finished");
    }

    public static void mainx(String[] args) {
//        ServerSocket socket = new ServerSocket();
//        socket.accept();
    }
}