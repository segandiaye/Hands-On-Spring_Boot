package com.example.demo.configuration;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//@Configuration
//@EnableWebMvc
//@ComponentScan
public class WebMvcConfiguration //extends WebMvcConfigurerAdapter 
{
   /* @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ping").setViewName("ping2");
        //registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
   
       public class MvcConfiguration extends WebMvcConfigurerAdapter
	{
	    @Override
	    public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/resources/**");
		resolver.setSuffix(".html");
		resolver.setViewClass(JstlView.class);
		registry.viewResolver(resolver);
	    }
	}*/
}


/*
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

   @Override
   public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/").setViewName("index");
   }

   @Bean
   public ViewResolver viewResolver() {
      InternalResourceViewResolver bean = new InternalResourceViewResolver();

      bean.setViewClass(JstlView.class);
      bean.setPrefix("/WEB-INF/view/");
      bean.setSuffix(".jsp");

      return bean;
   }
}
*/
