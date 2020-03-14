package com.zeed.assignment.sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Encoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolManagementApplicationTests {

	@Test
	public void contextLoads() {

		char presentChar = 'Z';
		System.out.println("Next char is "+ Character.getName(presentChar + 1));
		BASE64Encoder base64Encoder = new BASE64Encoder();
		System.out.print("base 64 is " + base64Encoder.encode("user-management-service:secret".getBytes()));

	}

}
