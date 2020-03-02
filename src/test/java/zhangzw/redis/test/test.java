package zhangzw.redis.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhangzhuwei.util.DateUtil;
import com.zhangzhuwei.util.RandomUtil;
import com.zhangzhuwei.util.StringUtil;
import com.zhangzw.redis.test.pojo.User;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/redis.xml")
public class test {
		
	@Autowired
	private RedisTemplate redisTemplate;
	//生成十万条数据
	@Test
	public void test() throws ParseException {
		Date testopen = new Date();
		Integer key=1;
		HashMap<Integer, User> map = new HashMap<>();
		System.out.println("开始计时");
		Date teststart = new Date();
		System.out.println("======"+teststart);
		for (int i = 0; i <100; i++) {
			User u = new User();
			//设置id
			u.setId(i+1);
			//设置姓名
			String name = StringUtil.randomChineseString(3);
			u.setName(name);
			//设置性别
			int random = RandomUtil.random(1, 2);
			if(random==1) {
				u.setSex("男");
			}else {
				u.setSex("女");
			}
			//设置手机号
			int random2 = RandomUtil.random(100000000, 999999999);
			String phone2=String.valueOf(random2);
			String phone1="13"+phone2;
			long phone = Long.parseLong(phone1);
			u.setPhone(phone);
			//设置邮箱
			int j = RandomUtil.random(3,20);
			String e = RandomUtil.randomString(j);
			String email=e+"@163.com";
			u.setEmail(email);
			//设置生日
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String s="1949-00-00";
			String en="2001-12-31";
			Date start = sdf.parse(s);
			Date end = sdf.parse(en);
			Date birthday = DateUtil.randomDate(start, end);
			u.setBirthday(birthday);
			System.out.println(u);
			//第一个JDK系列化测试
			/*redisTemplate.opsForList().leftPush(key, u);*/
			map.put(u.getId(),u);			
		}
		//第三个测试hash类型保存
		redisTemplate.opsForHash().putAll(key, map);
		
		Date testend = new Date();
		long endtime = testend.getTime();
		System.out.println("===="+endtime);
		long starttime = teststart.getTime();
		System.out.println("==="+starttime);
		long time=endtime-starttime;
		System.out.println("hash类型保存");
		long count=redisTemplate.opsForHash().size("jdkTest");
		System.out.println("保存数量为"+count);
		System.out.println("所耗时间为"+time);
	}

}
